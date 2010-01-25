/**
 * 
 */
package com.hyk.rpc.core.service;

import com.hyk.rpc.core.remote.Remote;

/**
 * @author qiying.wang
 *
 */
@Remote
public  interface NameService {
	public Object getObject(String name);
	public boolean bind(String name, Object obj);
	public void deregister(String name);
}
