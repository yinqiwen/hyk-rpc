/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: ClassUtil.java 
 *
 * @author qiying.wang [ Jan 14, 2010 | 2:36:30 PM ]
 *
 */
package com.hyk.serializer.util;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ClassUtil
{
	/** A map from primitive types to their corresponding wrapper types. */
	public static final Map<Class<?>, Class<?>>	PRIMITIVE_TO_WRAPPER_TYPE;

	/** A map from wrapper types to their corresponding primitive types. */
	public static final Map<Class<?>, Class<?>>	WRAPPER_TO_PRIMITIVE_TYPE;

	static
	{
		Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
		Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);

		add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
		add(primToWrap, wrapToPrim, byte.class, Byte.class);
		add(primToWrap, wrapToPrim, char.class, Character.class);
		add(primToWrap, wrapToPrim, double.class, Double.class);
		add(primToWrap, wrapToPrim, float.class, Float.class);
		add(primToWrap, wrapToPrim, int.class, Integer.class);
		add(primToWrap, wrapToPrim, long.class, Long.class);
		add(primToWrap, wrapToPrim, short.class, Short.class);
		add(primToWrap, wrapToPrim, void.class, Void.class);

		PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
		WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
	}

	private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value)
	{
		forward.put(key, value);
		backward.put(value, key);
	}

	public static Constructor getConstructor(Class clazz, Object... paras) throws NoSuchMethodException
	{
		Constructor ret = null;
		Constructor[] allCons = clazz.getDeclaredConstructors();
		for(Constructor cons : allCons)
		{
			Class[] types = cons.getParameterTypes();
			if(types.length != paras.length)
				continue;
			boolean matched = true;
			for(int i = 0; i < types.length; i++)
			{
				if(null == paras[i])
					throw new NoSuchMethodException();
				Class declType = types[i];
				Class realType = paras[i].getClass();
				if(declType.isAssignableFrom(realType))
				{
					continue;
				}
				Class convertType = PRIMITIVE_TO_WRAPPER_TYPE.get(declType);
				if(null == convertType)
				{
					convertType = WRAPPER_TO_PRIMITIVE_TYPE.get(declType);
				}
				
				if(null != convertType)
				{
					
					if(convertType.equals(realType))
					{
						
						continue;
					}
				}
				matched = false;
				break;
			}
			if(matched)
			{		
				ret = cons;
			}
		}
		if(null == ret)
			throw new NoSuchMethodException();
		return ret;
	}
}
