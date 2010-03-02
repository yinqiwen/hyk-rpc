/**
 * 
 */
package rpc.common;

import com.hyk.rpc.core.annotation.Remote;

/**
 * @author Administrator
 *
 */
@Remote
public interface HelloIntf{

	public String sayHello(String name);
	
	public void noop();
}
