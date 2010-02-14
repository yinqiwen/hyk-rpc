/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Array;

import com.hyk.serializer.io.Type;
import com.hyk.serializer.reflect.ReflectionCache;
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
			Class componentTypeClass = type.getComponentType();
			
			Object array = Array.newInstance(componentTypeClass, len);
			ObjectReferenceUtil.addDeserializeThreadLocalObject(array);
			Type componentType = ReflectionCache.getType(componentTypeClass);
			while(index < len)
			{
				
				Object element = readObject(data, componentTypeClass);
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
		Class componentType = value.getClass().getComponentType();
		int index = 0;
		while(index < len)
		{
			Object element = Array.get(value, index);      
			writeObject(data, element, componentType);
			index++;
		}
		return data;
	}

}
