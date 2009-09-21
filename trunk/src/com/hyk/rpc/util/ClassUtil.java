/**
 * 
 */
package com.hyk.rpc.util;

import java.lang.reflect.Method;

import com.hyk.rpc.annotation.Remote;

/**
 * @author Administrator
 *
 */
public class ClassUtil {
	
	public static Method getMethod(int methodID, Class clazz)
	{
		Class c = getExposeInterface(clazz);
		if(null == c) return null;
		Method[] ms = c.getMethods();
		return ms[methodID];
	}
	
	public static Class getExposeInterface(Class clazz)
	{
		if(Object.class.equals(clazz)) return null;
		
		if(clazz.isInterface() && clazz.isAnnotationPresent(Remote.class))
		{
			return clazz;
		}
		
		Class[] interfaces = clazz.getInterfaces();
		for (Class inter : interfaces) {
			Class ret = getExposeInterface(inter);
			if(null != ret) return ret;
		}
		Class superClass = clazz.getSuperclass();
		return getExposeInterface(superClass);
	}
}
