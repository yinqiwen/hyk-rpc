/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.MessageFragment;

/**
 * @author Administrator
 *
 */
public class TCPRpcChannel extends AbstractDefaultRpcChannel {

	public TCPRpcChannel(Executor threadPool, int port) {
		super(threadPool);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#getRpcChannelAddress()
	 */
	@Override
	public Address getRpcChannelAddress() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#read()
	 */
	@Override
	protected RpcChannelData read() throws IOException{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#send(com.hyk.rpc.core.transport.RpcChannelData)
	 */
	@Override
	protected void send(RpcChannelData data) throws IOException{
		// TODO Auto-generated method stub

	}

}
