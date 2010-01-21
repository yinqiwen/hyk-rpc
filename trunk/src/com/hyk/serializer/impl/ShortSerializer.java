/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.serializer.AbstractSerailizerImpl;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 *
 */
public class ShortSerializer extends AbstractSerailizerImpl<Short> {


	@Override
	protected Short unmarshal(Class<Short> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		return readShort(data);
	}

	@Override
	protected ByteArray marshal(Short value, ByteArray data)
			throws NotSerializableException, IOException {
		writeInt(data, value);
		return data;
	}

}
