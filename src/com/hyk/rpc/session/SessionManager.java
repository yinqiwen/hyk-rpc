/**
 * 
 */
package com.hyk.rpc.session;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.RPC;
import com.hyk.rpc.address.RpcAddress;
import com.hyk.rpc.channel.RpcChannel;
import com.hyk.rpc.message.Request;
import com.hyk.rpc.message.Message;
import com.hyk.rpc.message.Response;

/**
 * @author Administrator
 *
 */
public class SessionManager {

	private RpcChannel channel;
	
	private Map<Long, Session>[] sessionMap = new Map[2];
	
	private List<Message> processingMessages = new LinkedList<Message>();
	
	public SessionManager(RpcChannel channel)
	{
		sessionMap[Session.CLIENT] = new ConcurrentHashMap<Long, Session>();
		sessionMap[Session.SERVER] = new ConcurrentHashMap<Long, Session>();
		this.channel = channel;
		new Thread(new ProcessMessageTask()).start();
		channel.setSessionManager(this);
		
	}
	
	
	
	public void dispatch(Message msg)
	{
		synchronized (processingMessages) {
			processingMessages.add(msg);
			processingMessages.notify();
		}
	}
	
	public RpcChannel getChannel()
	{
		return channel;
	}
	
	private Session createSession(Request request, int type, RpcAddress addr)
	{
		Session session = new Session(request, addr, channel);
		sessionMap[type].put(session.getId(), session);
		return session;
	}
	
	private Session getSession(int type, long sessionID)
	{
		return sessionMap[type].get(sessionID);
	}
	
	public Session createClientSession(Request request)
	{
		return createSession(request,Session.CLIENT, request.address);
	}
	
	public Session createServerSession(Request request)
	{
		return createSession(request, Session.SERVER, request.address);
	}
	
	class ProcessMessageTask implements Runnable
	{

		@Override
		public void run() {
			while(true)
			{
				try {
					Message msg = null;
					synchronized (processingMessages) {
						if(processingMessages.isEmpty())
						{
							processingMessages.wait();
						}
						msg = processingMessages.remove(0);
					}
					if(null != msg)
					{
						if(msg instanceof Request)
						{
							Request req = (Request) msg;
							Session session = createServerSession(req);
							session.processRequest();
						}
						else
						{
							Response res = (Response) msg;
							Session session = getSession(Session.CLIENT, res.getSessionID());
							if(null != session)
							{
								session.processResponse(res);
							}
							else
							{
								//do sth.
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
