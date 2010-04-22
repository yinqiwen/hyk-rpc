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
public class ByteSerializerStream extends SerailizerStream<Byte> {

	@Override
	protected Byte unmarshal(Class<Byte> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readByte(data);
	}

	@Override
	protected ByteDataBuffer marshal(Byte value,ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeByte(data, value);
		return data;
	}

}
