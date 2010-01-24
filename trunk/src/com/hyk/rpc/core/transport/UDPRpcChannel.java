/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class UDPRpcChannel extends RpcChannel {

	private ByteBuffer recvBuffer = ByteBuffer.allocate(65536);
	private ByteBuffer sendBuffer = ByteBuffer.allocate(65536);
	private DatagramChannel channel;
	private SimpleSockAddress localAddr;
	
	private Map<Long, MessageFragment[]> fragmentTable = new ConcurrentHashMap<Long, MessageFragment[]>();
	
	public UDPRpcChannel(Executor threadPool, int port) throws IOException {
		super(threadPool);
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		localAddr = new SimpleSockAddress(InetAddress.getLocalHost().getHostAddress(), port);
		start();
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#deleteMessageFragments(long)
	 */
	@Override
	protected void deleteMessageFragments(long sessionID) {
		fragmentTable.remove(sessionID);
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#getRpcChannelAddress()
	 */
	@Override
	public Address getRpcChannelAddress() {
		return localAddr;
	}
	

	@Override
	protected void saveMessageFragment(MessageFragment fragment) {
		MessageFragment[] fragments = fragmentTable.get(fragment.getSessionID());
		if(null == fragments)
		{
			fragments = new MessageFragment[fragment.getTotalFragmentCount()];
			fragmentTable.put(fragment.getSessionID(), fragments);
		}
		fragments[fragment.getSequence()] = fragment;

	}

	@Override
	protected MessageFragment[] loadMessageFragments(long sessionID) {
		return fragmentTable.get(sessionID);
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#read()
	 */
	@Override
	protected RpcChannelData read() {
	try {
			recvBuffer.clear();
			InetSocketAddress target = (InetSocketAddress) channel.receive(recvBuffer);
			recvBuffer.flip();
			SimpleSockAddress address = new SimpleSockAddress(target.getAddress().getHostAddress(), target.getPort());
			//System.out.println("###recv len:" + recvBuffer.limit());
			ByteArray data = ByteArray.allocate(recvBuffer.limit());
			data.put(recvBuffer);
			data.flip();
			return new RpcChannelData(data, address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}



	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#send(com.hyk.util.buffer.ByteArray)
	 */
	@Override
	protected void send(RpcChannelData data) {
		sendBuffer.clear();
		sendBuffer.put(data.content.buffer());
		sendBuffer.flip();
		try {
			//System.out.println("###write len:" + data.content.limit());
			SimpleSockAddress address = (SimpleSockAddress) data.address;
			InetSocketAddress addr = new InetSocketAddress(address.getHost(), address.getPort());
			channel.send(sendBuffer, addr);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
