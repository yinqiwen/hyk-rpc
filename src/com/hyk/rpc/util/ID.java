package com.hyk.rpc.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ID {
	
	private static AtomicLong objIdSeed = new AtomicLong(1);
	private static AtomicLong sessionIdSeed = new AtomicLong(1);
	private static AtomicInteger rpcIdSeed = new AtomicInteger(1);
	private static Map<Method, Integer> methodIDCache = new HashMap<Method, Integer>();
	
	public static long generateRemoteObjectID()
	{
		return objIdSeed.getAndAdd(1);
	}
	
	public static int generateRPCID()
	{
		return rpcIdSeed.getAndAdd(1);
	}
	
	public static long generateSessionID()
	{
		return sessionIdSeed.getAndAdd(1);
	}
	
	public static int getMethodID(Method method)
	{
		if(methodIDCache.containsKey(method))
		{
			return methodIDCache.get(method);
		}
		
		Class clazz = method.getDeclaringClass();
		Method[] ms = clazz.getMethods();
		for (int i = 0; i < ms.length; i++) {
			if(ms[i].equals(method))
			{
				methodIDCache.put(method, i);
				return i;
			}
		}
		return -1;
	}
	
	public static long generateRpcID()
	{
		UUID uuid = UUID.randomUUID();		
		return uuid.getMostSignificantBits();
	}

}
