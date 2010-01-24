/**
 * 
 */
package rpc.server;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.hyk.rpc.core.RPC;
import com.hyk.rpc.core.transport.UDPRpcChannel;

/**
 * @author Administrator
 *
 */
public class Server {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 48101);
		RPC rpc = new RPC(transport);
		rpc.getLocalNaming().bind("hello", new HelloImpl());
		
	}

}
