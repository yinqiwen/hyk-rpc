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
public class CharSerializer extends AbstractSerailizerImpl<Character> {

	@Override
	public Character unmarshal(Class<Character> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readChar(data);
	}

	@Override
	public ByteArray marshal(Character value, ByteArray data)
			throws NotSerializableException, IOException {
		writeChar(data, value);
		return data;
	}

}
