/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.hyk.serializer.SerializerInput;
import com.hyk.serializer.SerializerOutput;


/**
 * @author qiying.wang
 *
 */
public class Response extends AbstractMessageObject{

	//protected TypeValue reply;
	protected Object reply;
	
	public Object getReply() {
		return reply;
	}

	@Override
	public MessageType getType() {
		return MessageType.Response;
	}


	@Override
	public void readExternal(SerializerInput in) throws IOException {
		reply = in.readObject(Object.class);
		
	}

	@Override
	public void writeExternal(SerializerOutput out) throws IOException {
		out.writeObject(reply, Object.class);
	}
	
}
