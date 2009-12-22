/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.Externalizable;



/**
 * @author qiying.wang
 *
 */
public abstract class AbstractMessageObject implements Externalizable{

	public abstract MessageType getType();
	
}
