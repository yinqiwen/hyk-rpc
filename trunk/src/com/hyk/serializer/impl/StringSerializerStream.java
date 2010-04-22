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
public class StringSerializerStream extends SerailizerStream<String> {

	@Override
	protected String unmarshal(Class<String> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readString(data);
	}

	@Override
	protected ByteDataBuffer marshal(String value, ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeString(data, value);
		return data;
	}

}
