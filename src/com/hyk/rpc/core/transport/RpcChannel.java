/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.serializer.HykSerializer;
import com.hyk.serializer.Serializer;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 * 
 */
public abstract class RpcChannel {
	
	private static Logger logger = LoggerFactory.getLogger(RpcChannel.class);

	private int maxMessageSize = 4096;
	private List<MessageFragment> sendList = new LinkedList<MessageFragment>();
	private Serializer serializer = new HykSerializer();
	protected Executor threadPool;
	private List<MessageListener> msgListeners = new LinkedList<MessageListener>();

	private OutputTask outTask = new OutputTask();
	private InputTask inTask = new InputTask();

	public RpcChannel(Executor threadPool) {
		this.threadPool = threadPool;
	}
	
	public void start()
	{
		if(logger.isInfoEnabled())
		{
			logger.info("RpcChannel start.");
		}
		threadPool.execute(inTask);
		threadPool.execute(outTask);
	}

	public final void registerMessageListener(MessageListener listener)
	{
		msgListeners.add(listener);
	}
	
	public abstract Address getRpcChannelAddress();

	protected abstract void saveMessageFragment(MessageFragment fragment);

	protected abstract MessageFragment[] loadMessageFragments(long sessionID);

	protected abstract void deleteMessageFragments(long sessionID);

	protected abstract RpcChannelData read();

	protected abstract void send(RpcChannelData data);

	public final void sendMessage(Message message)
			throws NotSerializableException, IOException {
		ByteArray data = serializer.serialize(message);
//		try {
//			serializer.deserialize(Message.class, data);
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		int size = data.size();
		//System.out.println("msg size"+ size);
		int msgFragsount = size / maxMessageSize;
		if (size % maxMessageSize > 0) {
			msgFragsount++;
		}
		for (int i = 0; i < msgFragsount; i++) {
			ByteArray sent = data.subArray(0, Math.min(maxMessageSize, data
					.limit()));
//			try {
//				serializer.deserialize(Message.class, sent);
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			MessageFragment fragment = new MessageFragment();
			fragment.setAddress(message.getAddress());
			fragment.setSessionID(message.getSessionID());
			fragment.setSequence(i);
			fragment.setTotalFragmentCount(msgFragsount);
			fragment.setContent(sent);
			data = data.subArray(sent.limit());
			synchronized (sendList) {
				sendList.add(fragment);
				sendList.notify();
			}
		}
	}

	private void dispatch(final Message msg) {
		for (final MessageListener messageListener : msgListeners) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {	
					messageListener.onMessage(msg);
				}
			});
		}
	}

	class InputTask implements Runnable {
		// private ByteArrayInputStream bis;
		@Override
		public void run() {
			__start: while (true) {
				try {
					RpcChannelData data = read();
					MessageFragment fragment = serializer.deserialize(
							MessageFragment.class, data.content);
					if (fragment.getTotalFragmentCount() == 1
							&& fragment.getSequence() == 0) {
						Message msg = serializer.deserialize(Message.class,
								fragment.getContent());
						data.content.free();
						msg.setAddress(data.address);
						msg.setSessionID(fragment.getSessionID());
						dispatch(msg);
					} else {
						saveMessageFragment(fragment);
						MessageFragment[] fragments = loadMessageFragments(fragment
								.getSessionID());
						if (fragments.length != fragment
								.getTotalFragmentCount()) {
							throw new Exception("Error!");
						}
						for (int i = 0; i < fragments.length; i++) {
							if (null == fragments[i]) {
								continue __start;
							}
						}
						ByteArray msgBuffer = ByteArray.allocate(maxMessageSize
								* fragments.length);
						for (int i = 0; i < fragments.length; i++) {
							msgBuffer.put(fragments[i].getContent());
						}
						msgBuffer.flip();
						Message msg = serializer.deserialize(Message.class,
								msgBuffer);
						msg.setSessionID(fragment.getSessionID());
						msg.setAddress(data.address);
						msgBuffer.free();
						data.content.free();
						deleteMessageFragments(fragment.getSessionID());
						dispatch(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

	class OutputTask implements Runnable {

		public OutputTask() {
		}

		@Override
		public void run() {

			while (true) {
				try {
					MessageFragment msg = null;
					synchronized (sendList) {
						if (sendList.isEmpty()) {
							sendList.wait();
						}
						msg = sendList.remove(0);
					}
					if (null != msg) {
						ByteArray data = serializer.serialize(msg);
						RpcChannelData send = new RpcChannelData(data, msg.getAddress());
						//send.address = msg.ge
						// oos.writeObject(msg);
						// byte[] data = bos.toByteArray();
						send(send);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

	}
}
