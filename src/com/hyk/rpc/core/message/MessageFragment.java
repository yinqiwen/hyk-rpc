/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.IOException;

import com.hyk.rpc.core.address.Address;
import com.hyk.serializer.Externalizable;
import com.hyk.serializer.SerializerInput;
import com.hyk.serializer.SerializerOutput;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class MessageFragment implements Externalizable{
	
	public Address getAddress() {
		return id.address;
	}

	public void setAddress(Address address) {
		id.address = address;
	}

	public long getSessionID() {
		return id.sessionID;
	}

	public void setSessionID(long sessionID) {
		id.sessionID = sessionID;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getTotalFragmentCount() {
		return totalFragmentCount;
	}

	public void setTotalFragmentCount(int totalFragmentCount) {
		this.totalFragmentCount = totalFragmentCount;
	}

	public ByteArray getContent() {
		return content;
	}

	public void setContent(ByteArray content) {
		this.content = content;
	}
	//Address address;
	//long sessionID;
	MessageID id= new MessageID();
	public MessageID getId() {
		return id;
	}
	int sequence;
	int totalFragmentCount;
	ByteArray content;
	
	@Override
	public void readExternal(SerializerInput in) throws IOException {
		//sessionID = in.readLong();
		id = in.readObject(MessageID.class);
		sequence = in.readInt();
		totalFragmentCount = in.readInt();
		byte[] rawContent = in.readBytes();
		content = ByteArray.wrap(rawContent);
	}

	@Override
	public void writeExternal(SerializerOutput out) throws IOException {
		//out.writeLong(sessionID);
		out.writeObject(id);
		out.writeInt(sequence);
		out.writeInt(totalFragmentCount);
		out.writeBytes(content.rawbuffer(), content.position(), content.size());
	}

}
