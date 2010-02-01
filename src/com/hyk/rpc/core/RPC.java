/**
 * 
 */
package com.hyk.rpc.core;

import java.lang.reflect.Proxy;
import java.util.Map;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.remote.RemoteObjectFactory;
import com.hyk.rpc.core.remote.RemoteObjectProxy;
import com.hyk.rpc.core.service.NameService;
import com.hyk.rpc.core.service.NameServiceImpl;
import com.hyk.rpc.core.session.SessionManager;
import com.hyk.rpc.core.transport.RpcChannel;
import com.hyk.rpc.core.util.ID;

/**
 * @author Administrator
 * 
 */
public class RPC {

	private RpcChannel channel;
	private RemoteObjectFactory remoteObjectFactory;
	private SessionManager sessionManager;
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	private NameService nameService;

	public RPC(RpcChannel channel) {
		this.channel = channel;
		remoteObjectFactory = new RemoteObjectFactory(channel.getRpcChannelAddress());
		sessionManager = new SessionManager(channel, remoteObjectFactory);
		nameService = new NameServiceImpl(remoteObjectFactory);
	}
	
	public void setSessionTimeout(int sessionTimeout)
	{
		sessionManager.setSessionTimeout(sessionTimeout);
	}

	public NameService getLocalNaming()
	{
		return nameService;
	}
	
	public NameService getRemoteNaming(Address address)
	{
		RemoteObjectProxy proxy = new RemoteObjectProxy();
		proxy.setObjID(ID.NAMING_ID);
		proxy.setHostAddress(address);
		proxy.setSessionManager(sessionManager);
		NameService remoteNaming = (NameService) Proxy.newProxyInstance(NameService.class.getClassLoader(), new Class[]{NameService.class}, proxy);
		return remoteNaming;
	}
	
	public <T> T getRemoteService(Class<T> clazz, String name, Address address)
	{
		NameService remoteNaming = getRemoteNaming(address);
		return (T) remoteNaming.lookup(name);
	}
	
}
