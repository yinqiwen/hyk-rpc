/**
 * 
 */
package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.hyk.rpc.RPC;
import com.hyk.rpc.RpcException;
import com.hyk.rpc.address.RpcAddress;
import com.hyk.rpc.address.SocketRpcAddress;
import com.hyk.rpc.channel.UDPRpcChannel;
import com.hyk.rpc.naming.Naming;

import common.DelegateInter;
import common.TestService;

/**
 * @author qiying.wang
 *
 */
public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws RpcException 
	 */
	public static void main(String[] args) throws RpcException, IOException {
		// TODO Auto-generated method stub
		RpcAddress remote = new SocketRpcAddress(new InetSocketAddress("127.0.0.1",8971));
		RPC rpc = RPC.init(new UDPRpcChannel(new SocketRpcAddress(new InetSocketAddress(8973))));
		Naming naming = rpc.getRemoteNaming(remote);
		TestService test = (TestService) naming.lookup("test");
		System.out.println(test.sayHello());
		DelegateInter dt = test.getDelegateInter();
		System.out.println(dt.doSth(100));
	}

}
