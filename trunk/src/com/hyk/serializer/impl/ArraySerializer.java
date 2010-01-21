/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Array;

import com.hyk.serializer.AbstractSerailizerImpl;
import com.hyk.serializer.Serializer;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 * 
 */
public class ArraySerializer<T> extends AbstractSerailizerImpl<T>
{

	@Override
	protected T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException
	{
		try
		{
			int len = readInt(data);
			int index = 0;
			Object array = Array.newInstance(type.getComponentType(), len);
			Serializer serializer = SerializerImplFactory.getSerializer(type.getComponentType());
			while(index < len)
			{
				Array.set(array, index, serializer.deserialize(type.getComponentType(), data));
				index++;
			}
			return (T)array;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new IOException(e);
		}

	}

	@Override
	public ByteArray marshal(Object value, ByteArray data) throws NotSerializableException, IOException
	{
		int len = Array.getLength(value);
		writeInt(data, len);
		Serializer serializer = SerializerImplFactory.getSerializer(value.getClass().getComponentType());
		int index = 0;
		while(index < len)
		{
			serializer.serialize(Array.get(value, index), data);
			index++;
		}
		return data;
	}

}
