/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.rpc.core.message.MessageID;

/**
 * @author Administrator
 *
 */
public abstract class AbstractDefaultRpcChannel extends RpcChannel {

	protected  Logger			logger			= LoggerFactory.getLogger(getClass());
	
	protected Map<MessageID, MessageFragment[]> fragmentTable = new ConcurrentHashMap<MessageID, MessageFragment[]>();
	
	public AbstractDefaultRpcChannel(Executor threadPool) {
		super(threadPool);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.core.transport.RpcChannel#deleteMessageFragments(long)
	 */
	@Override
	protected void deleteMessageFragments(MessageID id) {
		fragmentTable.remove(id);
	}


	@Override
	protected MessageFragment[] loadMessageFragments(MessageID id) {
		return fragmentTable.get(id);
	}

	@Override
	protected void saveMessageFragment(MessageFragment fragment) {
		MessageFragment[] fragments = fragmentTable.get(fragment.getId());
		if(null == fragments)
		{
			fragments = new MessageFragment[fragment.getTotalFragmentCount()];
			fragmentTable.put(fragment.getId(), fragments);
		}
		if(null == fragments[fragment.getSequence()])
		{
			fragments[fragment.getSequence()] = fragment;
		}
		else
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Discard duplicate message fragment!");
			}
		}
		

	}

}
