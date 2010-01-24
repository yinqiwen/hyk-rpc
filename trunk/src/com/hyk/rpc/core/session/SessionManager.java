/**
 * 
 */
package com.hyk.rpc.core.session;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.message.Message;
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
public class SessionManager implements MessageListener {

	private static class ServerSessionID {
		Address address;
		long sessionID;

		public ServerSessionID(Address address, long sessionID) {
			this.address = address;
			this.sessionID = sessionID;
		}

		public boolean equals(Object anObject) {
			if (this == anObject) {
				return true;
			}
			if (anObject instanceof ServerSessionID) {
				ServerSessionID anotherServerSessionID = (ServerSessionID) anObject;

				return anotherServerSessionID.sessionID == sessionID
						&& anotherServerSessionID.address.equals(address);
			}
			return false;
		}

		public int hashCode() {
			return (int) (sessionID ^ (sessionID >>> 32)) + address.hashCode();
		}
	}

	//private Map<Long, Session>[] sessionMap = new Map[2];

	private Map<Long, Session> clientSessionMap = new ConcurrentHashMap<Long, Session>();
	private Map<ServerSessionID, Session> serverSessionMap = new ConcurrentHashMap<ServerSessionID, Session>();

	private List<Message> processingMessages = new LinkedList<Message>();
	private RpcChannel channel;
	private RemoteObjectFactory remoteObjectFactory;

	public SessionManager(RpcChannel channel,
			RemoteObjectFactory remoteObjectFactory) {
		//sessionMap[Session.CLIENT] = new ConcurrentHashMap<Long, Session>();
		//sessionMap[Session.SERVER] = new ConcurrentHashMap<Long, Session>();
		this.channel = channel;
		this.remoteObjectFactory = remoteObjectFactory;
		channel.registerMessageListener(this);
		// new Thread(new ProcessMessageTask()).start();

	}

	public void dispatch(Message msg) {
		synchronized (processingMessages) {
			processingMessages.add(msg);
			processingMessages.notify();
		}
	}

	private Session createSession(Message request, int type) {
		Session session = new Session(this,request, type, channel,
				remoteObjectFactory);
		switch (type) {
		case Session.CLIENT:
		{
			clientSessionMap.put(request.getSessionID(), session);
			break;
		}
		case Session.SERVER:
		{
			ServerSessionID sessionID = new ServerSessionID(request.getAddress(), request.getSessionID());
			serverSessionMap.put(sessionID, session);
			break;
		}
		default:
			break;
		}
		return session;
	}

	public Session removeClientSession(long sessionID) {
		return clientSessionMap.remove(sessionID);
	}
	
	public void removeServerSession(Session session)
	{
		Message request = session.request;
		serverSessionMap.remove(new ServerSessionID(request.getAddress(), request.getSessionID()));
	}

	public Session createClientSession(Message request) {
		return createSession(request, Session.CLIENT);
	}

	public Session createServerSession(Message request) {
		return createSession(request, Session.SERVER);
	}

	@Override
	public void onMessage(Message msg) {

		switch (msg.getType()) {
		case Request: {
			// Request req = (Request) msg.getValue();
			Session session = createServerSession(msg);
			session.processRequest();
			break;
		}
		case Response: {
			// Response res = (Response) msg.getValue();
			Session session = removeClientSession(msg.getSessionID());
			// System.out.println("####get " + msg.getSessionID());
			session.processResponse(msg);
			break;
		}
		default:
			break;
		}
	}
}
