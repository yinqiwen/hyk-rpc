/**
 * 
 */
package com.hyk.rpc.naming;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.rpc.RPC;
import com.hyk.rpc.RpcException;

/**
 * @author Administrator
 *
 */
public class NamingImpl implements Naming {

	private Map<String, Object> objTable = new ConcurrentHashMap<String, Object>();
	private RPC rpc;
	private NamingImpl()
	{
		
	}
	
	public NamingImpl(RPC rpc)
	{
		this.rpc = rpc;
	}
	
	@Override
	public boolean bind(String name, Object obj) {
		// TODO Auto-generated method stub
		if(objTable.containsKey(name))
		{
			return false;
		}
		else
		{
			try {
				objTable.put(name, rpc.expose(obj));
			} catch (RpcException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.naming.Naming#loopup(java.lang.String)
	 */
	@Override
	public Object lookup(String name) {
		// TODO Auto-generated method stub
		return objTable.get(name);
	}

}
