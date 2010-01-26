/**
 * 
 */
package rpc.server;

import rpc.common.HelloIntf;

/**
 * @author Administrator
 *
 */
public class HelloImpl implements HelloIntf {

	/* (non-Javadoc)
	 * @see rpc.common.HelloIntf#sayHello(java.lang.String)
	 */
	@Override
	public String sayHello(String name) {
		
		return "Hello," + name;
	}

	@Override
	public void noop()
	{
		// TODO Auto-generated method stub
		
	}

}
