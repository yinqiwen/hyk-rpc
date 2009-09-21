/**
 * 
 */
package server;

import com.hyk.rpc.RPC;
import com.hyk.rpc.RpcException;

import common.DelegateInter;
import common.TestService;


/**
 * @author qiying.wang
 *
 */
public class ServiceImpl implements TestService{

	@Override
	public String sayHello() {
		// TODO Auto-generated method stub
		return "What a suprise!";
	}

	@Override
	public DelegateInter getDelegateInter() {
		// TODO Auto-generated method stub
		try {
			return (DelegateInter) RPC.exposeRpcObject(new DelegateImpl());
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
