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
public class Request implements Externalizable{

	
	protected long sessionID;
	protected long objID;
	protected int operationID;

	protected TypeValue[] args;
	
	@Override
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {
		sessionID = in.readLong();
		objID = in.readLong();
		operationID = in.readInt();
		args = in.readObject(TypeValue[].class);
		
	}
	
	@Override
	public void writeExternal(Output out) throws IOException {
		out.writeLong(sessionID);
		out.writeLong(objID);
		out.writeInt(operationID);
		out.writeObject(args);
	}
	
	
}