/**
 * 
 */
package com.hyk.rpc.naming;

import com.hyk.rpc.annotation.Remote;

/**
 * @author Administrator
 *
 */
@Remote
public interface Naming {

	public Object lookup(String name);
	
	public boolean bind(String name, Object obj);
}
