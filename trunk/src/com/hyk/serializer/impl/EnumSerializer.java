/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.serializer.AbstractSerailizerImpl;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 *
 */
public class EnumSerializer extends AbstractSerailizerImpl<Enum> {

	/* (non-Javadoc)
	 * @see com.hyk.serializer.Serializer#deserialize(java.lang.Class, com.hyk.util.buffer.ByteArray)
	 */
	@Override
	protected Enum unmarshal(Class<Enum> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		String name = readString(data);
		return Enum.valueOf(type, name);
	}

	/* (non-Javadoc)
	 * @see com.hyk.serializer.Serializer#serialize(java.lang.Object, com.hyk.util.buffer.ByteArray)
	 */
	@Override
	protected ByteArray marshal(Enum value, ByteArray data)
			throws NotSerializableException, IOException {
		writeString(data, value.name());
		return data;
	}

}
