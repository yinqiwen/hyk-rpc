/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Array;

import com.hyk.serializer.util.ObjectReferenceUtil;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 * 
 */
public class ArraySerializerStream<T> extends SerailizerStream<T>
{

	@Override
	protected T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException
	{
		try
		{
			int len = readInt(data);
			int index = 0;
			Object array = Array.newInstance(type.getComponentType(), len);
			ObjectReferenceUtil.addDeserializeThreadLocalObject(array);
			while(index < len)
			{
				Object element = readObject(data, type.getComponentType());
				Array.set(array, index, element);
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
	protected ByteArray marshal(Object value,ByteArray data) throws NotSerializableException, IOException
	{
		int len = Array.getLength(value);
		writeInt(data, len);
		
		int index = 0;
		while(index < len)
		{
			Object element = Array.get(value, index);      
			writeObject(data, element, value.getClass().getComponentType());
			index++;
		}
		return data;
	}

}
