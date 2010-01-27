/**
 * 
 */
package rpc.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;

import rpc.common.HelloIntf;

import com.hyk.rpc.core.RPC;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.service.NameService;
import com.hyk.rpc.core.transport.TCPRpcChannel;
import com.hyk.rpc.core.transport.UDPRpcChannel;
import com.hyk.rpc.core.util.CommonUtil;

/**
 * @author Administrator
 *
 */
public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 48100);
		TCPRpcChannel transport = new TCPRpcChannel(Executors.newFixedThreadPool(10), 48100);
		RPC rpc = new RPC(transport);
		
		NameService serv = rpc.getRemoteNaming(new SimpleSockAddress(InetAddress.getLocalHost().getHostAddress(), 48101));
		HelloIntf hello = (HelloIntf) serv.lookup("hello");
		System.out.println(hello.sayHello("hyk-rpc!"));
		//hello.noop();
		System.exit(1);
	}

}
