/**
 * 
 */
package com.hyk.rpc;

import java.lang.reflect.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.address.RpcAddress;
import com.hyk.rpc.annotation.Remote;
import com.hyk.rpc.channel.RpcChannel;
import com.hyk.rpc.naming.Naming;
import com.hyk.rpc.naming.NamingImpl;
import com.hyk.rpc.session.SessionManager;
import com.hyk.rpc.util.CommonObjectUtil;
import com.hyk.rpc.util.ID;

/**
 * @author Administrator
 *
 */
public class RPC {
	
	//private SocketAddress address;
	private SessionManager sessionManager;
	private RpcChannel channel;
	private static Map<Long, Object> exposedRawObjectTable = new ConcurrentHashMap<Long, Object>();
	
	//private static List<RPC> rpcList = new ArrayList<RPC>();
	private static Map<RpcAddress, RPC> rpcTable = new HashMap<RpcAddress, RPC>();
	
	private RPC(RpcChannel channel) throws RpcException
	{
		this.channel = channel;
		sessionManager = new SessionManager(channel);
		channel.setRpc(this);
		channel.start();
		rpcTable.put(channel.getAddress(), this);
		RpcObjectProxy.proxyLocal(new NamingImpl(this), this, Constants.ReservedID.NAMING_OBJ_ID);
	}
	
	public RpcChannel getRpcChannel()
	{
		return channel;
	}
	
	public SessionManager getSessionManager()
	{
		return sessionManager;
	}
	
	public static RPC init(RpcChannel channel) throws RpcException
	{	
		return new RPC(channel);
	}
	
	public Naming getRemoteNaming(RpcAddress address) throws RpcException
	{
		return RpcObjectProxy.proxyRemote(Naming.class, Constants.ReservedID.NAMING_OBJ_ID, address, this);
	}
	
    public Naming getLocalNaming()
    {
    	return (Naming) exposedRawObjectTable.get(Constants.ReservedID.NAMING_OBJ_ID);
    }
    
	static void bindRawObj(long id, Object rawObj)
	{
		exposedRawObjectTable.put(id, rawObj);
	}
	
	public static Object id2Object(long id)
	{
		return exposedRawObjectTable.get(id);
	}
	
	public static long object2Id(Object rpcObj)throws RpcException
	{
		Class clazz = rpcObj.getClass();
		if(Proxy.isProxyClass(clazz) && Proxy.getInvocationHandler(rpcObj).getClass().equals(RpcObjectProxy.class))
		{
			RpcObjectProxy proxy = (RpcObjectProxy) Proxy.getInvocationHandler(rpcObj);
			return proxy.getObjectID();
		}
		throw new RpcException("Argument is not a RPC object!");
	}
	
	public Object expose(Object obj)throws RpcException
	{
		if(null == obj) return null;
		Class clazz = obj.getClass();
		if(Proxy.isProxyClass(clazz) && Proxy.getInvocationHandler(obj).getClass().equals(RpcObjectProxy.class))
		{
			return obj;
		}
		else
		{
			return RpcObjectProxy.proxyLocal(obj, this);
		}
	}
	
	public static Object exposeRpcObject(Object obj) throws RpcException
	{				
		if(rpcTable.size() > 0)
		{
			RPC rpc = rpcTable.entrySet().iterator().next().getValue();
			return rpc.expose(obj);
		}
		throw new RpcException("No RPC init!");
	}
	
	public static Object exposeRpcObject(Object obj, RpcAddress address) throws RpcException
	{				
		RPC rpc = rpcTable.get(address);
		if(null == rpc)
		{
			throw new RpcException("No RPC init!");
		}
		return rpc.expose(obj);
	}
	
	
}
