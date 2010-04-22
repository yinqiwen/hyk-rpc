/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.util.buffer.ByteArray;
import com.hyk.io.ByteDataBuffer;

/**
 * @author qiying.wang
 *
 */
public class EnumSerializerStream extends SerailizerStream<Enum> {


	@Override
	protected Enum unmarshal(Class<Enum> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		String name = readString(data);
		return Enum.valueOf(type, name);
	}

	@Override
	protected ByteDataBuffer marshal(Enum value,ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeString(data, value.name());
		return data;
	}

}
