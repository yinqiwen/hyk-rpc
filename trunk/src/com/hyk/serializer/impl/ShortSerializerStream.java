/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.io.ByteDataBuffer;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 *
 */
public class ShortSerializerStream extends SerailizerStream<Short> {


	@Override
	protected Short unmarshal(Class<Short> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readShort(data);
	}

	@Override
	protected ByteDataBuffer marshal(Short value, ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeInt(data, value);
		return data;
	}

}
