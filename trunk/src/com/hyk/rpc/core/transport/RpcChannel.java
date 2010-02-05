/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.compress.Compressor;
import com.hyk.compress.CompressorFactory;
import com.hyk.compress.CompressorType;
import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.rpc.core.message.MessageID;
import com.hyk.rpc.core.message.MessageType;
import com.hyk.rpc.core.session.SessionManager;
import com.hyk.serializer.HykSerializer;
import com.hyk.serializer.Serializer;
import com.hyk.serializer.impl.SerailizerStream;
import com.hyk.util.buffer.ByteArray;
import com.hyk.util.thread.ThreadLocalUtil;

/**
 * @author qiying.wang
 * 
 */
public abstract class RpcChannel
{
	protected Logger				logger			= LoggerFactory.getLogger(getClass());

	protected static final byte[]	MAGIC_HEADER	= "@hyk-rpc@".getBytes();
	protected static final int		GAP				= 32;

	protected byte[]				magicHeader		= new byte[MAGIC_HEADER.length];

	protected int					maxMessageSize	= 2048;

	protected List<MessageFragment>	sendList		= new LinkedList<MessageFragment>();
	protected Serializer			serializer		= new HykSerializer();
	protected Executor				threadPool;
	protected SessionManager		sessionManager;

	protected List<MessageListener>	msgListeners	= new LinkedList<MessageListener>();

	protected OutputTask			outTask			= new OutputTask();
	protected InputTask				inTask			= new InputTask();
	protected boolean				isStarted		= false;

	protected CompressorType		compressorType	= CompressorType.GZ;
	protected int					compressTrigger	= 256;

	public RpcChannel()
	{
		this(null);
	}

	public RpcChannel(Executor threadPool)
	{
		this.threadPool = threadPool;
	}

	public final void setCompressorType(CompressorType type)
	{
		this.compressorType = type;
	}

	public final void setCompressTrigger(int trigger)
	{
		this.compressTrigger = trigger;
	}

	public synchronized void start()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("RpcChannel start.");
		}
		if(isStarted)
		{
			return;
		}
		isStarted = true;
		if(null != threadPool)
		{
			threadPool.execute(inTask);
			threadPool.execute(outTask);
		}

	}

	public void setSessionManager(SessionManager sessionManager)
	{
		this.sessionManager = sessionManager;
		registerMessageListener(sessionManager);
	}

	public final void registerMessageListener(MessageListener listener)
	{
		msgListeners.add(listener);
	}

	public void setMaxMessageSize(int maxMessageSize)
	{
		this.maxMessageSize = maxMessageSize;
	}

	public abstract Address getRpcChannelAddress();

	public abstract boolean isReliable();

	protected abstract void saveMessageFragment(MessageFragment fragment);

	protected abstract MessageFragment[] loadMessageFragments(MessageID id);

	protected abstract void deleteMessageFragments(MessageID id);

	protected abstract RpcChannelData read() throws IOException;

	protected abstract void send(RpcChannelData data) throws IOException;

	public void close()
	{
		// nothing
	}

	public final void sendMessage(Message message) throws NotSerializableException, IOException
	{

		ByteArray data = serializer.serialize(message);
		int size = data.size();
		if(logger.isDebugEnabled())
		{
			logger.debug("send message " + message.getValue() + " to " + message.getAddress().toPrintableString() + " with total size:" + size);
		}
		int msgFragsount = size / maxMessageSize;
		if(size % maxMessageSize > 0)
		{
			msgFragsount++;
		}

		int off = 0;
		int len = 0;
		for(int i = 0; i < msgFragsount; i++)
		{
			ByteArray sent = null;
			if(msgFragsount == 1)
			{
				sent = data;
			}
			else
			{
				if(size - off >= maxMessageSize)
				{
					len = maxMessageSize;
				}
				else
				{
					len = size - off;
				}
				off += len;
				sent = ByteArray.allocate(len);
				data.get(sent);
				sent.flip();
			}

			MessageFragment fragment = new MessageFragment();
			fragment.setAddress(message.getAddress());
			fragment.setSessionID(message.getSessionID());
			fragment.setSequence(i);
			fragment.setTotalFragmentCount(msgFragsount);
			fragment.setContent(sent);
			if(logger.isDebugEnabled())
			{
				logger.debug("Send message with size:" + sent.size() + ", fragments count:" + msgFragsount);
			}
			if(null != threadPool)
			{
				synchronized(sendList)
				{
					sendList.add(fragment);
					sendList.notify();
				}
			}
			else
			{
				sendMessageFragment(fragment);
			}
		}
		if(msgFragsount > 1)
		{
			data.free();
		}
	}

	public final void processIncomingData(RpcChannelData data) throws RpcChannelException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Process message from " + data.address.toPrintableString());
		}
		Message msg = processRpcChannelData(data);
		if(msg != null)
		{
			for(final MessageListener messageListener : msgListeners)
			{
				messageListener.onMessage(msg);
			}
		}
	}

	private void dispatch(final Message msg)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Dispatch message!");
		}

		for(final MessageListener messageListener : msgListeners)
		{
			threadPool.execute(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						messageListener.onMessage(msg);
					}
					catch(Exception e)
					{
						logger.error("Failed to process message.", e);
					}

				}
			});
		}
	}

	protected void sendMessageFragment(MessageFragment msg) throws IOException
	{
		// ByteArray data = ByteArray.allocate(maxMessageSize + GAP);
		// data.put(MAGIC_HEADER);
		ByteArray data = ByteArray.allocate(maxMessageSize + GAP);
		data.put(MAGIC_HEADER);
		
		ByteArray seriaData = ByteArray.allocate(maxMessageSize);
		seriaData = serializer.serialize(msg, seriaData);
		
		if(seriaData.size() > compressTrigger)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Send/Before compressing, data size:" + seriaData.size());
			}
			SerailizerStream.writeInt(data, compressorType.getValue());
			ByteArray newData = CompressorFactory.getCompressor(compressorType).compress(seriaData);
			if(logger.isDebugEnabled())
			{
				logger.debug("Send/After compressing, data size:" + newData.size());
			}
			if(newData != seriaData)
			{
				seriaData.free();
			}
			data.put(newData);
			newData.free();
		}
		else
		{
			SerailizerStream.writeInt(data, CompressorType.NONE.getValue());
			data.put(seriaData);
			seriaData.free();
		}

		data.flip();
		RpcChannelData send = new RpcChannelData(data, msg.getAddress());
		send(send);
		
		data.free();
	}

	protected synchronized Message processRpcChannelData(RpcChannelData data) throws RpcChannelException
	{
		ByteArray oldContent = data.content;
		oldContent.get(magicHeader);
		if(!Arrays.equals(magicHeader, MAGIC_HEADER))
		{
			throw new RpcChannelException("Unexpected rpc message!");
		}
		try
		{
			int compressorTypeValue = SerailizerStream.readInt(oldContent);
			Compressor compressor = CompressorFactory.getCompressor(CompressorType.valueOf(compressorTypeValue));
			if(logger.isDebugEnabled())
			{
				logger.debug("Recv/Before decompressing, data size:" + oldContent.size());
			}
			ByteArray content = compressor.decompress(oldContent);
			if(logger.isDebugEnabled())
			{
				logger.debug("Recv/After decompressing, data size:" + content.size());
			}
			if(content != oldContent)
			{
				oldContent.free();
			}
			ThreadLocalUtil.getThreadLocalUtil(SessionManager.class).setThreadLocalObject(sessionManager);
			MessageFragment fragment = serializer.deserialize(MessageFragment.class, content);
			fragment.setAddress(data.address);
			if(logger.isDebugEnabled())
			{
				logger.debug("Recv " + fragment + " with size:" + content.size());
			}
			if(fragment.getTotalFragmentCount() == 1 && fragment.getSequence() == 0)
			{
				Message msg = serializer.deserialize(Message.class, fragment.getContent());
				content.free();
				msg.setAddress(data.address);
				msg.setSessionID(fragment.getSessionID());
				return msg;
			}

			saveMessageFragment(fragment);
			MessageFragment[] fragments = loadMessageFragments(fragment.getId());
			if(fragments.length != fragment.getTotalFragmentCount())
			{
				throw new RpcChannelException("Error!");
			}
			for(int i = 0; i < fragments.length; i++)
			{
				if(null == fragments[i])
				{
					if(logger.isDebugEnabled())
					{
						logger.debug("Message is not ready to process since element:" + i + " is null.");
					}
					return null;
				}
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Message is ready to process!");
			}
			ByteArray msgBuffer = ByteArray.allocate(maxMessageSize * fragments.length);
			for(int i = 0; i < fragments.length; i++)
			{
				msgBuffer.put(fragments[i].getContent());
			}
			msgBuffer.flip();

			Message msg = serializer.deserialize(Message.class, msgBuffer);
			msg.setSessionID(fragment.getSessionID());
			msg.setAddress(data.address);
			msgBuffer.free();
			data.content.free();
			deleteMessageFragments(fragment.getId());
			return msg;
		}
		catch(RpcChannelException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new RpcChannelException("Failed to process message!", e);
		}

	}

	class InputTask implements Runnable
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					RpcChannelData data = read();
					Message msg = processRpcChannelData(data);
					if(null != msg)
					{
						dispatch(msg);
					}
				}
				catch(Throwable e)
				{
					logger.error("Failed to process received message", e);
				}
			}
		}
	}

	class OutputTask implements Runnable
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					MessageFragment msg = null;
					synchronized(sendList)
					{
						if(sendList.isEmpty())
						{
							sendList.wait();
						}
						msg = sendList.remove(0);
					}
					if(null != msg)
					{
						sendMessageFragment(msg);
					}
				}
				catch(Throwable e)
				{
					logger.error("Failed to send message", e);
				}
			}
		}
	}
}
