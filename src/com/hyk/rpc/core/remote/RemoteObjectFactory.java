/**
 * 
 */
package com.hyk.rpc.core.remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.util.ID;
import com.hyk.rpc.core.util.RemoteUtil;

/**
 * @author qiying.wang
 * 
 */
public class RemoteObjectFactory
{
	private Address				localAddress;
	private Map<Long, Object>	remoteRawObjectTable	= new ConcurrentHashMap<Long, Object>();

	public RemoteObjectFactory(Address localAddress)
	{
		this.localAddress = localAddress;
	}

	public boolean remove(Object obj)
	{
		if(Proxy.isProxyClass(obj.getClass()))
		{
			InvocationHandler handler = Proxy.getInvocationHandler(obj);
			if(handler instanceof RemoteObjectProxy)
			{
				long id = ((RemoteObjectProxy)handler).getObjID();
				remoteRawObjectTable.remove(id);
				return true;
			}
		}
		return false;
	}
	
	public Object publish(Object obj)
	{
		return publish(obj, ID.generateRemoteObjectID());
	}

	public Object publish(Object obj, long id)
	{
		RemoteObjectProxy remoteObjectProxy = new RemoteObjectProxy();
		remoteObjectProxy.setHostAddress(localAddress);
		Object proxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(), RemoteUtil.getRemoteInterfaces(obj.getClass()), remoteObjectProxy);
		remoteObjectProxy.setObjID(id);
		remoteRawObjectTable.put(id, obj);
		return proxy;
	}

	public Object getRawObject(long id)
	{
		return remoteRawObjectTable.get(id);
	}
	
	public long getRemoteObjectId(Object obj) 
	{
		if(Proxy.isProxyClass(obj.getClass()))
		{
			InvocationHandler handler = Proxy.getInvocationHandler(obj);
			if(handler instanceof RemoteObjectProxy)
			{
				long id = ((RemoteObjectProxy)handler).getObjID();
				return id;
			}
		}
		return 0;
	}
	
	public Object getRawObject(Object obj)
	{
		if(Proxy.isProxyClass(obj.getClass()))
		{
			InvocationHandler handler = Proxy.getInvocationHandler(obj);
			if(handler instanceof RemoteObjectProxy)
			{
				long id = ((RemoteObjectProxy)handler).getObjID();
				return remoteRawObjectTable.get(id);
			}
		}
		return null;
	}

}
