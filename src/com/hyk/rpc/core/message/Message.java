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
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {
		sessionID = in.readLong();
		int typeValue = in.readInt();
		type = MessageType.valueOf(typeValue);
		switch (type) {
		case Request:
		{
			value = in.readObject(Request.class);
			break;
		}	
		case Response:
		{
			value = in.readObject(Response.class);
			break;
		}
		case Exception:
		{
			break;
		}
		default:
			break;
		}
	}
	@Override
	public void writeExternal(Output out) throws IOException {
		out.writeLong(sessionID);
		out.writeInt(type.getValue());
		out.writeObject(value);		
	}
	
	
}
