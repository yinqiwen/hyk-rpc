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
public class UDPRpcChannel extends AbstractDefaultRpcChannel
{
	private ByteBuffer			recvBuffer	= ByteBuffer.allocate(65536);
	private DatagramChannel		channel;
	private SimpleSockAddress	localAddr;

	public UDPRpcChannel(Executor threadPool, int port) throws IOException
	{
		super(threadPool);
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		localAddr = new SimpleSockAddress(InetAddress.getLocalHost().getHostAddress(), port);
		start();
	}

	@Override
	public Address getRpcChannelAddress()
	{
		return localAddr;
	}

	@Override
	protected RpcChannelData read() throws IOException
	{

		recvBuffer.clear();
		InetSocketAddress target = (InetSocketAddress)channel.receive(recvBuffer);
		recvBuffer.flip();
		SimpleSockAddress address = new SimpleSockAddress(target.getAddress().getHostAddress(), target.getPort());
		// System.out.println("###recv len:" + recvBuffer.limit());
		ByteArray data = ByteArray.allocate(recvBuffer.limit());
		data.put(recvBuffer);
		data.flip();
		return new RpcChannelData(data, address);

	}

	@Override
	protected void send(RpcChannelData data) throws IOException
	{
		SimpleSockAddress address = (SimpleSockAddress)data.address;
		InetSocketAddress addr = new InetSocketAddress(address.getHost(), address.getPort());
		channel.send(data.content.buffer(), addr);
	}

	@Override
	public boolean isReliable()
	{
		return false;
	}

}
