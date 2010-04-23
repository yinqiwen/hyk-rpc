/**
 * 
 */
package rpc.server;

import rpc.common.HelloIntf;

/**
 * @author Administrator
 * 
 */
public class HelloImpl implements HelloIntf
{

	@Override
	public String sayHello(String name)
	{
		try
		{
			Thread.sleep(12);
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
		// throw new NullPointerException("sdasd");

	}

}
