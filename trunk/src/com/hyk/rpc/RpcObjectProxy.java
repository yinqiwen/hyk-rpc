/**
 * 
 */
package com.hyk.rpc;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.SocketAddress;

import com.hyk.rpc.RPC;
import com.hyk.rpc.RpcException;
import com.hyk.rpc.address.RpcAddress;
import com.hyk.rpc.message.Request;
import com.hyk.rpc.message.Response;
import com.hyk.rpc.session.Session;
import com.hyk.rpc.session.SessionManager;
import com.hyk.rpc.util.ClassUtil;
import com.hyk.rpc.util.CommonObjectUtil;
import com.hyk.rpc.util.ID;

/**
 * @author Administrator
 *
 */
public class RpcObjectProxy implements InvocationHandler, Externalizable {

	public long getObjID() {
		return objID;
	}
	private long objID;
	private RpcAddress address;
	private transient RPC rpc;
	//private transient static ExposeObjectFactory exposeObjectFactory = ExposeObjectFactory.getInstance();
	
	public RpcObjectProxy(){
		
	}
	
//	private RpcObjectProxy(Object obj, SocketAddress address)
//	{
//		this.value = obj;
//		this.address = address;
//		this.objID = ID.generateRemoteObjectID();
//		
//	}
	
	RpcObjectProxy(long objID, RpcAddress address, RPC rpc)
	{
		this.objID = objID;
		this.address = address;
		this.rpc = rpc;
	}
	
	
	public long getObjectID()
	{
		return objID;
	}
	
	static Object proxyLocal(Object obj, RPC rpc) throws RpcException
	{
		return proxyLocal(obj, rpc, ID.generateRemoteObjectID());
	}
	
	static Object proxyLocal(Object obj, RPC rpc, long id) throws RpcException
	{
		RpcObjectProxy proxy =  new RpcObjectProxy(id, rpc.getRpcChannel().getAddress(), rpc);
		
		Class interfaceClazz = ClassUtil.getExposeInterface(obj.getClass());
		if(null == interfaceClazz)
		{
			throw new RpcException("Can not expose object if it is not implement Remote!");
		}
		RPC.bindRawObj(proxy.objID, obj);
		return Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class[]{interfaceClazz}, proxy);
	}
	
	static <T> T proxyRemote(Class<T> clazz, long remoteObjID, RpcAddress addr, RPC rpc) throws RpcException
	{
		RpcObjectProxy proxy =  new RpcObjectProxy(remoteObjID, addr,rpc);
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
	}
	
	private Object invokeLocal(Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		return method.invoke(RPC.id2Object(objID), args);
	}
	
	private Object invokeRemote(Method method, Object[] args) throws Throwable
	{
		Request request = new Request();
		request.setArgs(args);
		request.setObjID(objID);
		request.setMethodID(ID.getMethodID(method));
		request.address = address;
		Session session = rpc.getSessionManager().createClientSession(request);
		session.sendRequest();
		Response res = session.waitResponse();
	    if(res.isSuccess())
	    {
	    	return res.getReturnObj();
	    }
	    else
	    {
	    	throw new RpcException(res.getExtra());
	    }
	}

	

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		//method.ge
		if(!address.equals(rpc.getRpcChannel().getAddress()))
		{
			return invokeRemote(method, args);	
		}
		else
		{
			
			return invokeLocal(method, args);
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		//obj = (RpcObject) in.readObject();
		//obj.reverse();
//		RPC rpc = CommonObjectUtil.getRpc();
//		sessionManager = rpc.getSessionManager();
//		exposeObjectFactory = rpc.getExposeObjectFactory();
		objID = in.readLong();
		address = (RpcAddress) in.readObject();
		rpc = CommonObjectUtil.getRpc();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		//out.writeObject(obj);
		out.writeLong(objID);
		out.writeObject(address);
	}

}
