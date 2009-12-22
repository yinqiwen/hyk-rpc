/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * @author qiying.wang
 *
 */
public class Message implements Externalizable{
	protected long sessionID;
	protected MessageType type;
	protected AbstractMessageObject value;
	
	
	public MessageType getType()
	{
		return type;
	}
	
	public AbstractMessageObject getValue()
	{
		return value;
	}


	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	
}
