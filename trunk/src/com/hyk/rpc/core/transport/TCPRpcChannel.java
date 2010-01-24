/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.util.concurrent.Executor;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.MessageFragment;

/**
 * @author Administrator
 *
 */
public class TCPRpcChannel extends RpcChannel {

	public TCPRpcChannel(Executor threadPool, int port) {
		super(threadPool);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#deleteMessageFragments(long)
	 */
	@Override
	protected void deleteMessageFragments(long sessionID) {
		// TODO Auto-generated method stub

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
	 * @see com.hyk.rpc.core.transport.RpcChannel#loadMessageFragments(long)
	 */
	@Override
	protected MessageFragment[] loadMessageFragments(long sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#read()
	 */
	@Override
	protected RpcChannelData read() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#saveMessageFragment(com.hyk.rpc.core.message.MessageFragment)
	 */
	@Override
	protected void saveMessageFragment(MessageFragment fragment) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#send(com.hyk.rpc.core.transport.RpcChannelData)
	 */
	@Override
	protected void send(RpcChannelData data) {
		// TODO Auto-generated method stub

	}

}
