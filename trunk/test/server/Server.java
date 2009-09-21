package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.hyk.rpc.RPC;
import com.hyk.rpc.RpcException;
import com.hyk.rpc.address.SocketRpcAddress;
import com.hyk.rpc.channel.UDPRpcChannel;
import com.hyk.rpc.naming.Naming;

public class Server {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws RpcException 
	 */
	public static void main(String[] args) throws RpcException, IOException {
		// TODO Auto-generated method stub
       RPC rpc = RPC.init(new UDPRpcChannel(new SocketRpcAddress(new InetSocketAddress("127.0.0.1",8971))));
       Naming naming = rpc.getLocalNaming();
       naming.bind("test", new ServiceImpl());
	}

}
