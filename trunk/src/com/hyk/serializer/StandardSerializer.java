package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.serializer.impl.OtherSerializerStream;
import com.hyk.serializer.impl.SerailizerStreamFactory;
import com.hyk.util.buffer.ByteArray;

public class StandardSerializer implements Serializer
{


	public ByteArray serialize(Object obj) throws NotSerializableException, IOException
	{
		return serialize(obj, ByteArray.allocate(32));
	}

	public ByteArray serialize(Object obj, ByteArray input) throws NotSerializableException, IOException
	{
		SerailizerStreamFactory.otherSerializer.marshal(obj, input);
		return input.flip();
	}

	public <T> T deserialize(Class<T> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
	{
		OtherSerializerStream other = SerailizerStreamFactory.otherSerializer;
		Class clazz = type;
		T ret = (T)other.unmarshal(clazz, data);
		data.rewind();
		return ret;
	}

}
