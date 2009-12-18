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
public class Request extends AbstractMessageObject{

	protected long objID;
	protected int operationID;

	protected TypeValue[] args;
	
	@Override
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {
		objID = in.readLong();
		operationID = in.readInt();
		args = in.readObject(TypeValue[].class);
		
	}
	
	@Override
	public void writeExternal(Output out) throws IOException {
		out.writeLong(objID);
		out.writeInt(operationID);
		out.writeObject(args);
	}

	@Override
	public MessageType getType() {
		return MessageType.Request;
	}
	
	
}
