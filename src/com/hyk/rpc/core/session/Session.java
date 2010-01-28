/**
 * 
 */
package com.hyk.rpc.core.session;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.rpc.core.RpcException;
import com.hyk.rpc.core.Rpctimeout;
import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageFactory;
import com.hyk.rpc.core.message.Request;
import com.hyk.rpc.core.message.Response;
import com.hyk.rpc.core.remote.RemoteObjectFactory;
import com.hyk.rpc.core.transport.RpcChannel;
import com.hyk.rpc.core.util.CommonUtil;
import com.hyk.rpc.core.util.ID;
import com.hyk.rpc.core.util.RemoteUtil;
import com.hyk.util.reflect.ClassUtil;

/**
 * @author Administrator
 * 
 */
public class Session
{
	protected Logger			logger			= LoggerFactory.getLogger(getClass());
	public static final int		CLIENT			= 0;
	public static final int		SERVER			= 1;

	private static final int	DEFAULT_TIMEOUT	= 100000;

	private int					type;
	private Object				waitResLock		= new Object();

	Message						request;
	Message						response;
	private RpcChannel			channel;
	private RemoteObjectFactory	remoteObjectFactory;
	private SessionManager		sessionManager;

	public Session(SessionManager manager, Message request, int type, RpcChannel channel, RemoteObjectFactory remoteObjectFactory)
	{
		this.sessionManager = manager;
		this.request = request;
		this.type = type;
		this.channel = channel;
		this.remoteObjectFactory = remoteObjectFactory;
		// request.setSessionID(id);
	}

	public void sendRequest() throws NotSerializableException, IOException
	{
		// request.setSessionID(id);
		channel.sendMessage(request);
		// channel.sendMessage(request);
	}

	public void sendResponse(Message response) throws NotSerializableException, IOException
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Send invocation result back!");
		}
		channel.sendMessage(response);
	}

	public Object waitInvokeResult() throws Throwable
	{
		if(null == response)
		{
			try
			{
				synchronized(waitResLock)
				{
					waitResLock.wait(DEFAULT_TIMEOUT);
				}
			}
			catch(Exception e)
			{
				throw new Rpctimeout(e.getMessage());
			}
			if(null == response)
			{
				throw new Rpctimeout("RPC timeout!");
			}
		}
		Response res = (Response)response.getValue();
		Object reply = res.getReply();
		if(reply != null && reply instanceof Throwable)
		{
			throw (Throwable) reply;
		}
		return reply;
	}

	public void processResponse(Message res)
	{
		this.response = res;
		synchronized(waitResLock)
		{
			waitResLock.notify();
		}
	}

	public void processRequest()
	{
		Request req = (Request)request.getValue();
		long objid = req.getObjID();
		Object[] paras = req.getArgs();
		try
		{
			
			Object target = remoteObjectFactory.getRawObject(objid);
			Method method = ClassUtil.getMethod(target.getClass(), req.getOperation(), paras);
			if(logger.isDebugEnabled())
			{
				logger.debug("execute invocation:" +method.getName() + ", paras:" + Arrays.toString(paras));
			}
			Object result = null;
			try 
			{
				 result = method.invoke(target, paras);
			}
			catch(InvocationTargetException e)
			{
				e.getCause().getStackTrace();
				result = e.getCause();		
			}
			catch (Throwable e) 
			{
				e.getCause().getStackTrace();
				result = e;
			}
			
			if(logger.isDebugEnabled())
			{
				logger.debug("Invoked finish with result:" + result);
			}
			response = MessageFactory.instance.createResponse(request, result);
			sendResponse(response);
			sessionManager.removeServerSession(this);
		}
		catch(Exception e)
		{
			logger.error("Failed to execute invocation", e);
		}
	}
}
