/**
 * 
 */
package com.hyk.serializer.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiying.wang
 *
 */
public class ReflectionCache {

	private static Map<Class, Field[]> fieldCacheTable = new ConcurrentHashMap<Class, Field[]>();
	
	
	public static Field[] getDeclaredFields(Class clazz)
	{
		Field[] fs = fieldCacheTable.get(clazz);
		if(null == fs)
		{
			fs = clazz.getDeclaredFields();
			fieldCacheTable.put(clazz, fs);
			for (Field field : fs) {
				field.setAccessible(true);
			}
		}
		return fs;
	}
	
}
