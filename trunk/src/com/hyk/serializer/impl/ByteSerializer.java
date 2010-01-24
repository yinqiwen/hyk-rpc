/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 *
 */
public class ByteSerializer extends AbstractSerailizerImpl<Byte> {

	@Override
	public Byte unmarshal(Class<Byte> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readByte(data);
	}

	/* (non-Javadoc)
	 * @see com.hyk.serializer.Serializer#serialize(java.lang.Object, com.hyk.util.buffer.ByteArray)
	 */
	@Override
	public ByteArray marshal(Byte value,ByteArray data)
			throws NotSerializableException, IOException {
		writeByte(data, value);
		return data;
	}

}
