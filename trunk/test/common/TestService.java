package common;

import com.hyk.rpc.annotation.Remote;

/**
 * 
 */

/**
 * @author qiying.wang
 *
 */
@Remote
public interface TestService {

	public String sayHello();
	public DelegateInter getDelegateInter();
}
