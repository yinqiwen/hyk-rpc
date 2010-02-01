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
public class FloatSerializerStream extends SerailizerStream<Float> {

	@Override
	protected Float unmarshal(Class<Float> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readFloat(data);
	}

	//@Override
	protected ByteArray marshal(Float value,ByteArray data)
			throws NotSerializableException, IOException {
		writeFloat(data, value);
		return data;
	}

}