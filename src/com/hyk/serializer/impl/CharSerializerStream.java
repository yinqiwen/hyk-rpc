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
public class CharSerializerStream extends SerailizerStream<Character> {

	@Override
	protected Character unmarshal(Class<Character> type, ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readChar(data);
	}

	@Override
	protected ByteDataBuffer marshal(Character value, ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeChar(data, value);
		return data;
	}

}
