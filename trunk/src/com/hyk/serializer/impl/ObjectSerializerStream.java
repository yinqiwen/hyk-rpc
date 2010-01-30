/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


import com.hyk.serializer.Externalizable;
import com.hyk.serializer.Serializer;
import com.hyk.serializer.io.Type;
import com.hyk.serializer.reflect.ReflectionCache;
import com.hyk.serializer.util.ObjectReferenceUtil;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 * 
 */
public class ObjectSerializerStream<T> extends SerailizerStream<T>
{	
	@Override
	protected T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
	{
		try
		{
			T ret = (T)ReflectionCache.getDefaultConstructor(type).newInstance(null);
			
			if(!(ret instanceof Serializable))
			{
				throw new NotSerializableException(type.getName());
			}
			ObjectReferenceUtil.addDeserializeThreadLocalObject(ret);
			if (ret instanceof Externalizable) {
				Externalizable externalizable = (Externalizable) ret;
				externalizable.readExternal(new Input(data));
				return ret;
			}
			Field[] fs = ReflectionCache.getSerializableFields(type);
			while(true)
			{
				int tag = readTag(data);
				if(tag == 0)
					break;
				Field f = fs[tag - 1];
				Class fieldType = f.getType();
				f.set(ret, readObject(data, fieldType));
			}
			return ret;
		}
		catch(IllegalAccessException e)
		{
			throw new IOException(e);
		}
		catch(InvocationTargetException e)
		{
			throw new IOException(e.getCause());
		}
	}

	@Override
	protected ByteArray marshal(T value, ByteArray data) throws NotSerializableException, IOException
	{	
		Class clazz = value.getClass();
		if(!(value instanceof Serializable))
		{
			throw new NotSerializableException(clazz.getName());
		}
		if (value instanceof Externalizable) {
			Externalizable externalizable = (Externalizable) value;
			externalizable.writeExternal(new Output(data));
			return data;
		}
	
		try
		{
			//writeInt(data, 0);
			Field[] fs = ReflectionCache.getSerializableFields(clazz);
			for(int i = 0; i < fs.length; i++)
			{
				Field f = fs[i];
				Object fieldValue = f.get(value);
				if(null != fieldValue)
				{
					writeTag(data, i + 1);
					writeObject(data, fieldValue, f.getType());
					
				}
			}
			writeTag(data, 0);
		}
		catch(IllegalAccessException e)
		{
			throw new IOException(e);
		}

		return data;
	}

}
