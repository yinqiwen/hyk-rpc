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
public class DoubleSerializer extends AbstractSerailizerImpl<Double> {

	@Override
	public Double unmarshal(Class<Double> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readDouble(data);
	}

	@Override
	public ByteArray marshal(Double value, ByteArray data)
			throws NotSerializableException, IOException {
		writeDouble(data, value);
		return data;
	}

}
