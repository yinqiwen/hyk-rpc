/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.IOException;

import com.hyk.serializer.Externalizable;
import com.hyk.serializer.HykSerializer.Input;
import com.hyk.serializer.HykSerializer.Output;

/**
 * @author qiying.wang
 *
 */
public class Response extends AbstractMessageObject{

	protected TypeValue reply;
	@Override
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {

		reply = in.readObject(TypeValue.class);
	}
	@Override
	public void writeExternal(Output out) throws IOException {

		out.writeObject(reply);		
	}
	@Override
	public MessageType getType() {
		return MessageType.Response;
	}
	
}
