/**
 * 
 */
package com.hyk.rpc.core.session;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageID;
import com.hyk.rpc.core.message.Request;
import com.hyk.rpc.core.message.Response;
import com.hyk.rpc.core.remote.RemoteObjectFactory;
import com.hyk.rpc.core.transport.MessageListener;
import com.hyk.rpc.core.transport.RpcChannel;
import com.hyk.rpc.core.util.CommonUtil;

/**
 * @author Administrator
 * 
 */
public class SessionManager implements MessageListener
{
	protected Logger				logger				= LoggerFactory.getLogger(getClass());

	private Map<Long, Session>		clientSessionMap	= new ConcurrentHashMap<Long, Session>();
	private Map<MessageID, Session>	serverSessionMap	= new ConcurrentHashMap<MessageID, Session>();

	private List<Message>			processingMessages	= new LinkedList<Message>();
	private RpcChannel				channel;
	private RemoteObjectFactory		remoteObjectFactory;
	
	Timer timer = null;

	public SessionManager(RpcChannel channel, RemoteObjectFactory remoteObjectFactory)
	{
		this.channel = channel;
		this.remoteObjectFactory = remoteObjectFactory;
		channel.setSessionManager(this);
		if(!channel.isReliable())
		{
			timer = new Timer(SessionManager.class.getName(), true);
		}
	}

	public void dispatch(Message msg)
	{
		synchronized(processingMessages)
		{
			processingMessages.add(msg);
			processingMessages.notify();
		}
	}

	private Session createSession(Message request, int type)
	{
		Session session = new Session(this, request, type, channel, remoteObjectFactory);
		switch(type)
		{
			case Session.CLIENT:
			{
				clientSessionMap.put(request.getSessionID(), session);
				break;
			}
			case Session.SERVER:
			{
				serverSessionMap.put(request.getId(), session);
				break;
			}
			default:
				break;
		}
		return session;
	}

	public Session removeClientSession(long sessionID)
	{
		return clientSessionMap.remove(sessionID);
	}
	
	public Session getClientSession(long sessionID)
	{
		return clientSessionMap.get(sessionID);
	}
	
	public Session getServerSession(MessageID id)
	{
		return serverSessionMap.get(id);
	}

	public void removeServerSession(Session session)
	{
		Message request = session.request;
		serverSessionMap.remove(request.getId());
	}

	public Session createClientSession(Message request)
	{
		return createSession(request, Session.CLIENT);
	}

	public Session createServerSession(Message request)
	{
		return createSession(request, Session.SERVER);
	}

	@Override
	public void onMessage(Message msg)
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Handle message:" + msg.getType());
		}
		switch(msg.getType())
		{
			case Request:
			{
				Session session = null;
				if(!serverSessionMap.containsKey(msg.getId()))
				{
					session = createServerSession(msg);
					
				}
				else
				{
					session = getServerSession(msg.getId());
					if(logger.isDebugEnabled())
					{
						logger.debug("Duplicate request!");
					}
				}
				session.processRequest();
				break;
			}
			case Response:
			{
				// Response res = (Response) msg.getValue();
				Session session = removeClientSession(msg.getSessionID());
				if(null != session)
				{
					session.processResponse(msg);
				}
				else
				{
					if(logger.isDebugEnabled())
					{
						logger.debug("Duplicate response!");
					}
				}
				break;
			}
			default:
				break;
		}
	}
}
