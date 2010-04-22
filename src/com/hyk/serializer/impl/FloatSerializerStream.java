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
public class FloatSerializerStream extends SerailizerStream<Float> {

	@Override
	protected Float unmarshal(Class<Float> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readFloat(data);
	}

	//@Override
	protected ByteDataBuffer marshal(Float value,ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeFloat(data, value);
		return data;
	}

}
