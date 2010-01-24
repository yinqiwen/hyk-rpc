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
	Address address;
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
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

	long sessionID;
	int sequence;
	int totalFragmentCount;
	ByteArray content;
	
	@Override
	public void readExternal(SerializerInput in) throws IOException {
		sessionID = in.readLong();
		sequence = in.readInt();
		totalFragmentCount = in.readInt();
		byte[] rawContent = in.readBytes();
		content = ByteArray.wrap(rawContent);
	}

	@Override
	public void writeExternal(SerializerOutput out) throws IOException {
		out.writeLong(sessionID);
		out.writeInt(sequence);
		out.writeInt(totalFragmentCount);
		out.writeBytes(content.rawbuffer(), content.position(), content.size());
	}

}
