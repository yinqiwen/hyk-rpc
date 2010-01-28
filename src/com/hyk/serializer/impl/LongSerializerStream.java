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
public class LongSerializerStream extends SerailizerStream<Long> {

	@Override
	protected Long unmarshal(Class<Long> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {

		return readLong(data);
	}

	@Override
	protected ByteArray marshal(Long value, ByteArray data)
			throws NotSerializableException, IOException {
		writeLong(data, value);
		return data;
	}

}
