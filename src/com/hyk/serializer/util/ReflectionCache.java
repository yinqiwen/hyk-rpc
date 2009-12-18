/**
 * 
 */
package com.hyk.serializer.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiying.wang
 *
 */
public class ReflectionCache {

	private static Map<Class, Field[]> fieldCacheTable = new ConcurrentHashMap<Class, Field[]>();
	private static Map<Class, Method[]> methodCacheTable = new ConcurrentHashMap<Class, Method[]>();
	
	protected static ArrayList<Field> getAllDeaclaredFields(Class clazz)
	{
		if(null == clazz || clazz.isPrimitive())
		{
			return null;
		}
		ArrayList<Field> ret = new ArrayList<Field>(32);
		while(!clazz.equals(Object.class))
		{
			Field[] fs = clazz.getDeclaredFields();
			for (Field field : fs) {
				field.setAccessible(true);
				ret.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		
		return ret;
		
	}
	
	public static Field[] getDeclaredFields(Class clazz)
	{
		Field[] fs = fieldCacheTable.get(clazz);
		if(null == fs)
		{
			ArrayList<Field> fieldList = getAllDeaclaredFields(clazz);
			fs = new Field[fieldList.size()];
			fieldList.toArray(fs);
			fieldCacheTable.put(clazz, fs);
		}
		return fs;
	}
	
	public static Method[] getMethods(Class clazz)
	{
		Method[] ms = methodCacheTable.get(clazz);
		if(null == ms)
		{
			ms = clazz.getMethods();
			methodCacheTable.put(clazz, ms);
		}
		return ms;
	}
	
}
