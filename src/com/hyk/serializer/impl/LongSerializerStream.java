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
public class LongSerializerStream extends SerailizerStream<Long> {

	@Override
	protected Long unmarshal(Class<Long> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {

		return readLong(data);
	}

	@Override
	protected ByteDataBuffer marshal(Long value, ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeLong(data, value);
		return data;
	}

}
