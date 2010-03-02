/**
 * 
 */
package rpc.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;

import rpc.common.HelloAsyncIntf;
import rpc.common.HelloIntf;

import com.hyk.rpc.core.RPC;
import com.hyk.rpc.core.RpcCallback;
import com.hyk.rpc.core.RpcException;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.service.NameService;
import com.hyk.rpc.core.transport.TCPRpcChannel;
import com.hyk.rpc.core.transport.UDPRpcChannel;
import com.hyk.rpc.core.util.CommonUtil;
import com.hyk.rpc.core.util.RpcUtil;

/**
 * @author Administrator
 *
 */
public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws RpcException 
	 */
	public static void main(String[] args) throws IOException, RpcException {
		UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 48100);
		//TCPRpcChannel transport = new TCPRpcChannel(Executors.newFixedThreadPool(10), 48100);
		RPC rpc = new RPC(transport);
		
		NameService serv = rpc.getRemoteNaming(new SimpleSockAddress(InetAddress.getLocalHost().getHostAddress(), 48101));
		HelloIntf hello = (HelloIntf) serv.lookup("hello");
		HelloAsyncIntf aync = RpcUtil.asyncWrapper(hello, HelloAsyncIntf.class);
		
		long start = System.currentTimeMillis();
		for(int i =0; i< 10000; i++)
		{
			hello.noop();
//			aync.noop(new RpcCallback<Void>()
//			{	
//				@Override
//				public void run(Void parameter, Throwable e)
//				{
//					
//				}
//			});
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println(hello.sayHello("hyk-rpc!"));
//		aync.sayHello("hyk-rpc!", new RpcCallback<String>()
//		{
//			@Override
//			public void run(String parameter, Throwable e)
//			{
//				System.out.println(parameter);
//				
//			}});
		System.exit(1);
	}

}
