/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Field;


import com.hyk.serializer.Externalizable;
import com.hyk.serializer.Serializer;
import com.hyk.serializer.io.Type;
import com.hyk.serializer.reflect.ReflectionCache;
import com.hyk.serializer.util.ObjectReferenceUtil;
import com.hyk.util.buffer.ByteArray;
import com.hyk.util.common.CommonUtil;

/**
 * @author qiying.wang
 * 
 */
public class ObjectSerializer<T> extends AbstractSerailizerImpl<T>
{	
	@Override
	public T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
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
		catch(Exception e)
		{
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public ByteArray marshal(T value, ByteArray data) throws NotSerializableException, IOException
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
					//writeBoolean(data, fieldValue != value);
					writeObject(data, fieldValue, f.getType());
					
				}
			}
			writeTag(data, 0);
		}
		catch(Exception e)
		{
			throw new IOException(e);
		}

		return data;
	}

}
