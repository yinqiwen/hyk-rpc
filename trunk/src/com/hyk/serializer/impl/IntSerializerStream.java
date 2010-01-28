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
public class IntSerializerStream extends SerailizerStream<Integer>{

	@Override
	protected ByteArray marshal(Integer obj,  ByteArray data)
			throws NotSerializableException, IOException {
		writeInt(data, obj);
		return data;
	}

	protected Integer unmarshal(Class<Integer> type,ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readInt(data);
	}



}
