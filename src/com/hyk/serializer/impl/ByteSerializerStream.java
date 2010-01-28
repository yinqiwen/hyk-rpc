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
public class ByteSerializerStream extends SerailizerStream<Byte> {

	@Override
	protected Byte unmarshal(Class<Byte> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readByte(data);
	}

	@Override
	protected ByteArray marshal(Byte value,ByteArray data)
			throws NotSerializableException, IOException {
		writeByte(data, value);
		return data;
	}

}
