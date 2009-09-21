/**
 * 
 */
package com.hyk.rpc.address;

import java.io.Serializable;

/**
 * @author qiying.wang
 *
 */
public abstract class RpcAddress implements Serializable{

	public abstract int hashCode();
	public abstract boolean equals(Object obj);
}
