/**
 * 
 */
package com.hyk.rpc.core.remote;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author qiying.wang
 *
 */
public class RemoteObjectProxy implements InvocationHandler {

	private long objID;
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}



}
