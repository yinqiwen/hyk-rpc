/**
 * 
 */
package com.hyk.rpc.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.hyk.rpc.RPC;
import com.hyk.rpc.address.RpcAddress;
import com.hyk.rpc.message.Message;
import com.hyk.rpc.serializer.JavaSerializer;
import com.hyk.rpc.serializer.ObjectSerializer;
import com.hyk.rpc.session.SessionManager;
import com.hyk.rpc.util.CommonObjectUtil;

/**
 * @author Administrator
 * 
 */
public abstract class RpcChannel {

    private OutputTask outTask ;
    private InputTask inTask;
    protected ObjectSerializer serializer = new JavaSerializer();
    private List<Message> sendList = new LinkedList<Message>();
    
    protected SessionManager sessionManager;
	protected RPC rpc;
    protected RpcAddress address;
     
    public void setSessionManager(SessionManager s)
    {
    	sessionManager = s;
    }
    
    public void setRpc(RPC rpc)
    {
    	this.rpc = rpc;
    }
    
    public RpcAddress getAddress()
    {
    	return address;
    }
    
	public void sendMessage(Message msg)
	{
		synchronized (sendList) {
			sendList.add(msg);
			sendList.notify();
		}
	}

	protected class RpcData
	{
		byte[] data;
		RpcAddress address;
	}
	
	protected abstract RpcData readData();
	protected abstract void writeData(RpcData data);
	
	public RpcChannel(RpcAddress address) throws IOException {
		// ois = new ObjectInputStream(new ByteArrayInputStream(readBuffer));
		this.address = address;
		outTask = new OutputTask();
		inTask = new InputTask();		
	}
	
	public void start()
	{
		new Thread(outTask).start();
		new Thread(inTask).start();
	}

	private void dispatch(Message msg)
	{
		sessionManager.dispatch(msg);
	}
	
	class InputTask implements Runnable {
		//private ByteArrayInputStream bis;
		@Override
		public void run() {
			CommonObjectUtil.setRpc(rpc);
			while (true) {
				try {
					RpcData data = readData();
					Message msg = (Message) serializer.unmashall(data.data);
					msg.address = data.address;
					dispatch(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		}
		
	}
	
	class OutputTask implements Runnable {
		//private ByteArrayOutputStream bos = new ByteArrayOutputStream(65536);
		
		
		public OutputTask() throws IOException
		{
		}
		@Override
		public void run() {

			while (true) {
				try {
					//bos.reset();
					//ObjectOutputStream oos = new ObjectOutputStream(bos);
					Message msg = null;
					synchronized (sendList) {
						if(sendList.isEmpty())
						{
							sendList.wait();
						}
						msg = sendList.remove(0);
					}
					if(null != msg)
					{
						//oos.writeObject(msg);
						//byte[] data = bos.toByteArray();
						byte[] data = serializer.mashall(msg);
						RpcData sendData = new RpcData();
						sendData.data = data;
						sendData.address = msg.address;
						writeData(sendData);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

	}
}
