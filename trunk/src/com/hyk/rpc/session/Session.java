/**
 * 
 */
package com.hyk.rpc.session;

import java.lang.reflect.Method;
import java.net.SocketAddress;

import com.hyk.rpc.RPC;
import com.hyk.rpc.RpcException;
import com.hyk.rpc.address.RpcAddress;
import com.hyk.rpc.channel.RpcChannel;
import com.hyk.rpc.message.Request;
import com.hyk.rpc.message.Response;
import com.hyk.rpc.util.ClassUtil;
import com.hyk.rpc.util.ID;

/**
 * @author Administrator
 *
 */
public class Session {

	public static final int CLIENT = 0;
	public static final int SERVER = 1;
	
	private static final int DEFAULT_TIMEOUT = 10000;
	
	private final long id = ID.generateSessionID();
	private RpcAddress remoteAddress;
	private Object waitResLock = new Object();
	public long getId() {
		return id;
	}

	private Request request;
	private Response response;
	private RpcChannel channel;
	
	
	public Session(Request request, RpcAddress addr, RpcChannel channel)
	{
		this.request = request;
		this.channel = channel;
		this.remoteAddress = addr;
		request.setSessionID(id);
	}
	
	public void sendRequest()
	{
		request.address = remoteAddress;
		channel.sendMessage(request);
	}
	
	public void sendResponse(Response response)
	{
		response.address = remoteAddress;
		channel.sendMessage(response);
	}
	
	public Response waitResponse() throws RpcException
	{
		if(null != response) return response;
		try {
			synchronized (waitResLock) {
				waitResLock.wait(DEFAULT_TIMEOUT);
			}
		} catch (Exception e) {
			throw new RpcException(e.getMessage());
		}
		
		if(null == response)
		{
			throw new RpcException("RPC timeout!");
		}
		return response;
	}
	
	
	public void processResponse(Response res)
	{
		this.response = res;
		synchronized (waitResLock) {
			waitResLock.notify();
		}
		
	}
	
	public void processRequest()
	{
		response = request.createResponse();
		try {
			long objID = request.getObjID();
			int methodID = request.getMethodID();
			
			System.out.println("###Obj ID is " + objID);
			System.out.println("###session ID is " + request.getSessionID());
			Object target = RPC.id2Object(objID);
			Method method = ClassUtil.getMethod(methodID, target.getClass());
			
			Object ret = method.invoke(target, request.getArgs());
			
			response.setSuccess(true);
			response.setReturnObj(ret);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setExtra(e.getMessage());
		}
		
		channel.sendMessage(response);
	}
}
