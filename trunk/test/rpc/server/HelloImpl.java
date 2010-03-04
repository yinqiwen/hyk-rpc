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
		
		try
		{
			Thread.sleep(2000000);
		}
		catch(InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Hello," + name;
	}

	@Override
	public void noop()
	{
		//throw new NullPointerException("sdasd");
		
	}

}
