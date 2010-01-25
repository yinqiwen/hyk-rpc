/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 * 
 */
public class TCPRpcChannel extends AbstractDefaultRpcChannel
{
	protected Logger								logger			= LoggerFactory.getLogger(getClass());
	
	private ByteBuffer								recvBuffer		= ByteBuffer.allocate(65536);
	private ServerSocketChannel						channel;
	private List<RpcChannelData>					recvList		= new LinkedList<RpcChannelData>();
	private SimpleSockAddress						localAddr;
	private Selector								selector;
	private List<SelectionKey>						selectionKeys	= new ArrayList<SelectionKey>();
	private Map<SimpleSockAddress, SocketChannel>	socketTable		= new ConcurrentHashMap<SimpleSockAddress, SocketChannel>();

	public TCPRpcChannel(Executor threadPool, int port) throws IOException
	{
		super(threadPool);
		channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.socket().bind(new InetSocketAddress(port), 50);
		localAddr = new SimpleSockAddress(InetAddress.getLocalHost().getHostAddress(), port);

		selector = Selector.open();
		selectionKeys.add(channel.register(selector, SelectionKey.OP_ACCEPT));
		start();
	}

	@Override
	public void start()
	{
		threadPool.execute(new Runnable()
		{

			@Override
			public void run()
			{
				while(true)
				{
					try
					{
						int num = selector.select();						
						if(num > 0)
						{
							Set<SelectionKey> selectedKeys = selector.selectedKeys();
							Iterator<SelectionKey> it = selectedKeys.iterator();
							while(it.hasNext())
							{
								SelectionKey key = it.next();
								it.remove();
								if(key.isAcceptable())
								{			
									ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
									//ssc.
									SocketChannel csc = ssc.accept();
									InetSocketAddress target = (InetSocketAddress)csc.socket().getRemoteSocketAddress();
									if(logger.isDebugEnabled())
									{
										logger.debug("Accept a client from " + target);
									}
									SimpleSockAddress address = new SimpleSockAddress(target.getAddress().getHostAddress(), target.getPort());
									socketTable.put(address, csc);
									csc.configureBlocking(false);
									csc.register(selector, SelectionKey.OP_READ);
								}
								else if(key.isReadable())
								{
									if(logger.isDebugEnabled())
									{
										logger.debug("recv data... ");
									}
									SocketChannel csc = (SocketChannel)key.channel();
									//csc.configureBlocking(false);
									recvBuffer.clear();
									csc.read(recvBuffer);
									recvBuffer.flip();
									InetSocketAddress target = (InetSocketAddress)csc.socket().getRemoteSocketAddress();
									if(logger.isDebugEnabled())
									{
										logger.debug("recv data from " + target);
									}
									ByteArray data = ByteArray.allocate(recvBuffer.limit());
									data.put(recvBuffer);
									data.flip();
									
									SimpleSockAddress address = new SimpleSockAddress(target.getAddress().getHostAddress(), target.getPort());
									RpcChannelData recv = new RpcChannelData(data, address);
									synchronized(recvList)
									{
										recvList.add(recv);
										recvList.notify();
									}
								}
							}
						}

					}
					catch(IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		});
		super.start();
	}

	@Override
	public Address getRpcChannelAddress()
	{
		return localAddr;
	}

	@Override
	protected RpcChannelData read() throws IOException
	{
		synchronized(recvList)
		{
			if(recvList.isEmpty())
			{
				try
				{
					recvList.wait();
				}
				catch(InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			return recvList.remove(0);
		}
	}

	@Override
	protected void send(RpcChannelData data) throws IOException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Send data to " + data.address.toPrintableString());
		}
		SimpleSockAddress address = (SimpleSockAddress)data.address;
		SocketChannel csc = socketTable.get(address);
		if(null != csc)
		{
			csc.write(data.content.buffer());
		}
		else
		{
			csc = SocketChannel.open();
			InetSocketAddress target = new InetSocketAddress(address.getHost(), address.getPort());
			if(csc.connect(target))
			{
				csc.write(data.content.buffer());
				csc.configureBlocking(false);
				if(logger.isDebugEnabled())
				{
					logger.debug("Create socket to connect " + target);
				}
				socketTable.put(address, csc);
				csc.register(selector, SelectionKey.OP_READ);	
//				try
//				{
//					Thread.sleep(1000);
//				}
//				catch(InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
		}
	}

}
