/**
 * 
 */
package com.hyk.rpc.channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.hyk.rpc.address.SocketRpcAddress;


/**
 * @author Administrator
 *
 */
public class UDPRpcChannel extends RpcChannel {

	private DatagramChannel channel;
	private ByteBuffer recvBuffer = ByteBuffer.allocate(65536);
	private ByteBuffer sendBuffer = ByteBuffer.allocate(65536);
	public UDPRpcChannel(SocketRpcAddress address) throws IOException {
		super(address);
		//DatagramSocket sock = new DatagramSocket(port);
		channel = DatagramChannel.open();

		channel.socket().bind(address.getAddress());
	}
	

	/* (non-Javadoc)
	 * @see com.hyk.rpc.channel.RpcChannel#readData()
	 */
	@Override
	protected RpcData readData() {
		try {
			
			recvBuffer.clear();
			SocketAddress from = channel.receive(recvBuffer);
			recvBuffer.flip();
			byte[] ret = new byte[recvBuffer.limit()];
			System.out.println("###recv len:" + ret.length);
			recvBuffer.get(ret);
			RpcData data = new RpcData();
			data.data = ret;
			data.address = new SocketRpcAddress(from);
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.channel.RpcChannel#writeData(byte[])
	 */
	@Override
	protected void writeData(RpcData data) {
		// TODO Auto-generated method stub
		sendBuffer.clear();
		sendBuffer.put(data.data);
		sendBuffer.flip();
		try {
			SocketRpcAddress addr = (SocketRpcAddress) data.address;
			//System.out.println("###write addr:" + addr.getAddress());
			channel.send(sendBuffer, addr.getAddress());
			System.out.println("###write len:" + data.data.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
