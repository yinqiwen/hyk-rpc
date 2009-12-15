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
public class Response implements Externalizable{
	
	protected long sessionID;
	protected TypeValue reply;
	@Override
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {
		sessionID = in.readLong();
		reply = in.readObject(TypeValue.class);
	}
	@Override
	public void writeExternal(Output out) throws IOException {
		out.writeLong(sessionID);
		out.writeObject(reply);		
	}
	
}
