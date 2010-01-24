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
public class StringSerializer extends AbstractSerailizerImpl<String> {

	@Override
	public String unmarshal(Class<String> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readString(data);
	}

	@Override
	public ByteArray marshal(String value, ByteArray data)
			throws NotSerializableException, IOException {
		writeString(data, value);
		return data;
	}

}
