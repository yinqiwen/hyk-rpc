/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author qiying.wang
 *
 */
public class Request extends AbstractMessageObject{

	protected long objID;
	protected int operationID;

	protected TypeValue[] args;
	
	

	@Override
	public MessageType getType() {
		return MessageType.Request;
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
