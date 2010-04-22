package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.io.ByteDataBuffer;
import com.hyk.serializer.impl.OtherSerializerStream;
import com.hyk.serializer.impl.SerailizerStreamFactory;
import com.hyk.util.buffer.ByteArray;

public class StandardSerializer implements Serializer
{


	public ByteDataBuffer serialize(Object obj) throws NotSerializableException, IOException
	{
		return serialize(obj, ByteDataBuffer.allocate(256));
	}

	public ByteDataBuffer serialize(Object obj, ByteDataBuffer input) throws NotSerializableException, IOException
	{
		SerailizerStreamFactory.otherSerializer.marshal(obj, input);
		input.getOutputStream().close();
		return input;
	}

	public <T> T deserialize(Class<T> type, ByteDataBuffer data) throws NotSerializableException, IOException, InstantiationException
	{
		OtherSerializerStream other = SerailizerStreamFactory.otherSerializer;
		Class clazz = type;
		T ret = (T)other.unmarshal(clazz, data);
		data.reset();
		return ret;
	}

}
