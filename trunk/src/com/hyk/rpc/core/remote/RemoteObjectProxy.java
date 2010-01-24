/**
 * 
 */
package com.hyk.rpc.core.remote;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageFactory;
import com.hyk.rpc.core.session.Session;
import com.hyk.rpc.core.session.SessionManager;
import com.hyk.rpc.core.util.CommonUtil;
import com.hyk.rpc.core.util.ID;
import com.hyk.rpc.core.util.RemoteUtil;
import com.hyk.serializer.Externalizable;
import com.hyk.serializer.SerializerInput;
import com.hyk.serializer.SerializerOutput;

/**
 * @author qiying.wang
 *
 */
public class RemoteObjectProxy implements InvocationHandler, Serializable {

	private long objID = -2;
	private Address hostAddress;
	private transient SessionManager sessionManager;
	
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public Address getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(Address hostAddress) {
		this.hostAddress = hostAddress;
	}

	public long getObjID() {
		return objID;
	}

	public void setObjID(long objID) {
		this.objID = objID;
	}

	public RemoteObjectProxy()
	{
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		int methodID = RemoteUtil.getMethodID(method, proxy);
		//System.out.println("method is is" +methodID );
		//TypeValue[] paras = CommonUtil.buildTypeValues(args);
		Message msg = MessageFactory.instance.createRequest(objID, methodID, args);
		msg.setAddress(hostAddress);
		Session session = sessionManager.createClientSession(msg);
		session.sendRequest();
		//session.waitInvokeResult();
		Object ret =  session.waitInvokeResult();
		if(Proxy.isProxyClass(ret.getClass()))
		{
			Object handler = Proxy.getInvocationHandler(ret);
			if(handler instanceof RemoteObjectProxy)
			{
				((RemoteObjectProxy)handler).sessionManager = sessionManager;
			}
		}
		return ret;
	}



}
