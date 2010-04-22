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
public class DoubleSerializerStream extends SerailizerStream<Double> {

	@Override
	protected Double unmarshal(Class<Double> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readDouble(data);
	}

	@Override
	protected ByteDataBuffer marshal(Double value, ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeDouble(data, value);
		return data;
	}

}
