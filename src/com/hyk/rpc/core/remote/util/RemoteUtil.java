/**
 * 
 */
package com.hyk.rpc.core.remote.util;

import java.util.ArrayList;
import java.util.List;

import com.hyk.rpc.core.remote.Remote;

/**
 * @author qiying.wang
 *
 */
public class RemoteUtil {

	
	public static Class[] getRemoteInterfaces(Class clazz)
	{
		Class[] interfaces = clazz.getInterfaces();
	    List<Class> collect = new ArrayList<Class>();
	    for (Class cls : interfaces) {
			if(cls.isAnnotationPresent(Remote.class) || java.rmi.Remote.class.isAssignableFrom(cls))
			{
				collect.add(cls);
			}
		}
	    Class[] ret = new Class[collect.size()];
	    return collect.toArray(ret);
	}
}
