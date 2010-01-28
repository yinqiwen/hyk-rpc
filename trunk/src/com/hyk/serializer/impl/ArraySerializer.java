/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Array;

import com.hyk.serializer.Serializer;
import com.hyk.serializer.util.ObjectReferenceUtil;
import com.hyk.util.buffer.ByteArray;
import com.hyk.util.common.CommonUtil;

/**
 * @author qiying.wang
 * 
 */
public class ArraySerializer<T> extends AbstractSerailizerImpl<T>
{

	@Override
	public T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException
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
	public ByteArray marshal(Object value,ByteArray data) throws NotSerializableException, IOException
	{
		int len = Array.getLength(value);
		writeInt(data, len);
		
		int index = 0;
		while(index < len)
		{
			Object element = Array.get(value, index);      
//			if(value.getClass().getComponentType().equals(Object.class))
//			{
//				System.out.println("####" + element.getClass());
//			}
			writeObject(data, element, value.getClass().getComponentType());
			index++;
		}
		return data;
	}

}
