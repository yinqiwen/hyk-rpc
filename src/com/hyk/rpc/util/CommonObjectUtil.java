/**
 * 
 */
package com.hyk.rpc.util;

import com.hyk.rpc.RPC;

/**
 * @author Administrator
 *
 */
public class CommonObjectUtil {

	private static ThreadLocal<RPC> rpcInstanceTable = new ThreadLocal<RPC>();
	
	public static RPC getRpc()
	{
		return rpcInstanceTable.get();
	}
	
	public static void setRpc(RPC rpc)
	{
		rpcInstanceTable.set(rpc);
	}
}
