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
public class IntSerializerStream extends SerailizerStream<Integer>{

	@Override
	protected ByteDataBuffer marshal(Integer obj,  ByteDataBuffer data)
			throws NotSerializableException, IOException {
		writeInt(data, obj);
		return data;
	}

	protected Integer unmarshal(Class<Integer> type,ByteDataBuffer data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readInt(data);
	}



}
