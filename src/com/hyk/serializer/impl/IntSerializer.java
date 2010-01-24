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
public class IntSerializer extends AbstractSerailizerImpl<Integer>{

	@Override
	public ByteArray marshal(Integer obj,  ByteArray data)
			throws NotSerializableException, IOException {
		writeInt(data, obj);
		return data;
	}

	public Integer unmarshal(Class<Integer> type,ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readInt(data);
	}



}
