/**
 * 
 */
package rpc.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;

import rpc.common.HelloIntf;

import com.hyk.rpc.core.RPC;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.service.NameService;
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
		UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 48100);
		RPC rpc = new RPC(transport);
		NameService serv = rpc.getRemoteNaming(new SimpleSockAddress("192.168.1.100", 48101));
		//Object hello =  serv.getObject("hello");
		//CommonUtil.setSessionManager(rpc.getSessionManager());
		//System.out.println(Arrays.toString(hello.getClass().getInterfaces()));
		HelloIntf hello = (HelloIntf) serv.getObject("hello");
		System.out.println(hello.sayHello("hyk-rpc! This is first invoking!"));
		System.exit(1);
	}

}
