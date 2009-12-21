/**
 * 
 */
package com.hyk.rpc.core.remote;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.core.remote.util.RemoteUtil;

/**
 * @author qiying.wang
 *
 */
public class RemoteObjectFactory {

	private Map<Long, Object> remoteRawObjectTable = new ConcurrentHashMap<Long, Object>();
	
	public Object publish(Object obj)
	{
		
		Object proxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(), RemoteUtil.getRemoteInterfaces(obj.getClass()), new RemoteObjectProxy());
		return proxy;
	}
	
	public Object get(long id)
	{
		return remoteRawObjectTable.get(id);
	}
	
	
}
