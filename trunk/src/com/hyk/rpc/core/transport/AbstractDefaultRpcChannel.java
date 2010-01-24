/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.MessageFragment;

/**
 * @author Administrator
 *
 */
public abstract class AbstractDefaultRpcChannel extends RpcChannel {

	protected Map<Long, MessageFragment[]> fragmentTable = new ConcurrentHashMap<Long, MessageFragment[]>();
	
	public AbstractDefaultRpcChannel(Executor threadPool) {
		super(threadPool);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#deleteMessageFragments(long)
	 */
	@Override
	protected void deleteMessageFragments(long sessionID) {
		fragmentTable.remove(sessionID);
	}


	@Override
	protected MessageFragment[] loadMessageFragments(long sessionID) {
		return fragmentTable.get(sessionID);
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

}
