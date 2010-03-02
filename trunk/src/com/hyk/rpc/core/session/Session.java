/**
 * 
 */
package com.hyk.rpc.core.session;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.rpc.core.RequestListener;
import com.hyk.rpc.core.ResponseListener;
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
	protected Logger			logger					= LoggerFactory.getLogger(getClass());
	public static final int		CLIENT					= 0;
	public static final int		SERVER					= 1;

	private int					retransmitTimeStep		= 1000;
	private int					waitRetransmitTimeout	= retransmitTimeStep * 10;

	Message						request;
	Message						response;
	private RpcChannel			channel;
	private RemoteObjectFactory	remoteObjectFactory;
	private SessionManager		sessionManager;
	private RetransmitTimerTask	retransmitTask;

	private ResponseListener	responseListener;
	private RequestListener		requestListener;

	private long				bornTime				= System.currentTimeMillis();
	
	public Session(SessionManager manager, Message request, RpcChannel channel, RemoteObjectFactory remoteObjectFactory)
	{
		this.sessionManager = manager;
		this.request = request;
		this.channel = channel;
		this.remoteObjectFactory = remoteObjectFactory;
		// request.setSessionID(id);
	}

	public long getBornTime()
	{
		return bornTime;
	}
	
	public void setResponseListener(ResponseListener responseListener)
	{
		this.responseListener = responseListener;
	}

	public void setRequestListener(RequestListener requestListener)
	{
		this.requestListener = requestListener;
	}

	public void sendRequest() throws NotSerializableException, IOException
	{
		// request.setSessionID(id);
		channel.sendMessage(request);
		if(!channel.isReliable())
		{
			retransmitTask = new RetransmitTimerTask();
			sessionManager.timer.schedule(retransmitTask, retransmitTimeStep);
		}
	}

	public void sendResponse(Message response) throws NotSerializableException, IOException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Send invocation result back!");
		}
		channel.sendMessage(response);
	}

	public void processResponse(Message res)
	{
		this.response = res;
		if(null != responseListener)
		{
			try
			{
				responseListener.processResponse(this, (Response)res.getValue());
			}
			catch(Exception e)
			{
				logger.error("Failed to process response", e);
			}
		}
		close();

	}

	public void close()
	{
		if(null != retransmitTask)
		{
			retransmitTask.cancel();
		}
		channel.clearSessionData(request.getId());
	}

	public void processRequest()
	{
		if(response != null)
		{
			try
			{
				sendResponse(response);
			}
			catch(Exception e)
			{
				logger.error("Failed to resend response", e);
			}
			return;
		}

		Request req = (Request)request.getValue();
		long objid = req.getObjID();
		Object[] paras = req.getArgs();
		try
		{
			Object target = remoteObjectFactory.getRawObject(objid);
			if(null == target)
			{
				logger.error("Failed to get raw object with ID:" + objid + " messageID:" + request.getId());
			}
			Method method = ClassUtil.getMethod(target.getClass(), req.getOperation(), paras);
			if(logger.isDebugEnabled())
			{
				logger.debug("execute invocation:" + method.getName() + ", paras:" + Arrays.toString(paras));
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
			catch(Throwable e)
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
			if(channel.isReliable())
			{
				sessionManager.removeServerSession(this);
			}
			else
			{
				sessionManager.timer.schedule(new CleanTimerTask(), waitRetransmitTimeout);
			}

		}
		catch(Exception e)
		{
			logger.error("Failed to execute invocation", e);
		}
	}

	class CleanTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			sessionManager.removeServerSession(Session.this);
		}
	}

	class RetransmitTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			try
			{
				sendRequest();
			}
			catch(Throwable e)
			{
				logger.error("Failed to retransmit request", e);
			}
		}
	}

	class SessionTimeoutTask extends TimerTask
	{

		@Override
		public void run()
		{
			// Message res = new Message();
			// res
			// processResponse();
		}

	}
}
