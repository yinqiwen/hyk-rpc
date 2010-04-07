/**
 * 
 */
package rpc.server;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.hyk.rpc.core.RPC;
import com.hyk.rpc.core.RpcException;
import com.hyk.rpc.core.transport.TCPRpcChannel;
import com.hyk.rpc.core.transport.UDPRpcChannel;

/**
 * @author Administrator
 *
 */
public class Server {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws RpcException 
	 */
	public static void main(String[] args) throws IOException, RpcException {
		UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 48101);
		//TCPRpcChannel transport = new TCPRpcChannel(Executors.newFixedThreadPool(10), 48101);
		transport.setMaxMessageSize(20);
		RPC rpc = new RPC(transport);
		rpc.getLocalNaming().bind("hello", new HelloImpl());
		
	}

}
