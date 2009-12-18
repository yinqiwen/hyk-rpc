/**
 * 
 */
package com.hyk.rpc.core.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiying.wang
 *
 */
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

}
