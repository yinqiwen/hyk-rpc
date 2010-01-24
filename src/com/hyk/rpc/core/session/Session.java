/**
 * 
 */
package com.hyk.rpc.core.session;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Method;

import com.hyk.rpc.core.RpcException;
import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageFactory;
import com.hyk.rpc.core.message.Request;
import com.hyk.rpc.core.message.Response;
import com.hyk.rpc.core.remote.RemoteObjectFactory;
import com.hyk.rpc.core.transport.RpcChannel;
import com.hyk.rpc.core.util.CommonUtil;
import com.hyk.rpc.core.util.ID;
import com.hyk.rpc.core.util.RemoteUtil;

/**
 * @author Administrator
 *
 */
public class Session {

	public static final int CLIENT = 0;
	public static final int SERVER = 1;
	
	private static final int DEFAULT_TIMEOUT = 10000;
	
	private int type;
	private Object waitResLock = new Object();

	Message request;
	Message response;
	private RpcChannel channel;
	private RemoteObjectFactory remoteObjectFactory;
	private SessionManager sessionManager;
	
	public Session(SessionManager manager, Message request, int type, RpcChannel channel, RemoteObjectFactory remoteObjectFactory)
	{
		this.sessionManager = manager;
		this.request = request;
		this.type = type;
		this.channel = channel;
		this.remoteObjectFactory = remoteObjectFactory;
		//request.setSessionID(id);
	}
	
	public void sendRequest() throws NotSerializableException, IOException
	{
		//request.setSessionID(id);
		channel.sendMessage(request);
		//channel.sendMessage(request);
	}
	
	public void sendResponse(Message response) throws NotSerializableException, IOException
	{
		channel.sendMessage(response);
	}
	
	public Object waitInvokeResult() throws RpcException
	{
		if(null == response)
		{
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
		}
		Response res = (Response) response.getValue();
		return res.getReply();
	}
	
	public void processResponse(Message res)
	{
		this.response = res;
		synchronized (waitResLock) {
			waitResLock.notify();
		}
		
	}
	
	public void processRequest() 
	{
		Request req = (Request) request.getValue();
		long objid = req.getObjID();
		Object[] paras = req.getArgs();
		try {
			Object target = remoteObjectFactory.getRawObject(objid);
			Method method = RemoteUtil.getMethod(req.getOperationID(), target);
			Object result = method.invoke(target, paras);
			response = MessageFactory.instance.createResponse(request, result);
			response.setAddress(request.getAddress());
			sendResponse(response);
			sessionManager.removeServerSession(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
