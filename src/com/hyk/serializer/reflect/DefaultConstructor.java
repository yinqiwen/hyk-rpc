/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: DefaultConstructor.java 
 *
 * @author qiying.wang [ Jan 14, 2010 | 1:59:47 PM ]
 *
 */
package com.hyk.serializer.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class DefaultConstructor<T>
{
	Constructor<T> cons;
	Object[] defaultInitargs;
	
	public DefaultConstructor(Constructor<T> cons, Object[] defaultInitargs)
	{
		this.cons = cons;
		this.defaultInitargs = defaultInitargs;
	}
	
	public T newInstance() throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return cons.newInstance(defaultInitargs);
	}
}
