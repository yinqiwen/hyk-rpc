/**
 * 
 */
package com.hyk.rpc.core.transport;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.core.MessageListener;
import com.hyk.rpc.core.message.Message;

/**
 * @author qiying.wang
 *
 */
public class RpcChannel {

	private Map<Long, MessageListener> messageListeners = new ConcurrentHashMap<Long, MessageListener>();
	
	
	protected void processMessage(Message msg)
	{
		//MessageListener listener = messageListeners.get(msg.g);
	}
	
	public Object invoke(Method m, Object[] args)
	{
		return null;
	}
}
