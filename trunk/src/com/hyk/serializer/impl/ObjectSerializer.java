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
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 * 
 */
public class ObjectSerializer<T> extends AbstractSerailizerImpl<T>
{

//	public void init(ByteArray array) throws IOException
//	{
//		writeInt(array, 0);
//	}
	
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
				//AbstractSerailizerImpl serializer = SerializerImplFactory.getSerializer(fieldType);
				//f.set(ret, serializer.unmarshal(fieldType, data));
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
//		Class clazz = value.getClass();
//		if(clazz.equals(declType))
//		{
//			writeTag(data,0);
//		}
//		else
//		{
//			writeTag(data,1);
//			//System.out.println("####" + clazz.getName());
//			writeString(data,clazz.getName());
//		}	
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
					//AbstractSerailizerImpl serializer = SerializerImplFactory.getSerializer(f.getType());
					//serializer.marshal(fieldValue, f.getType(), data);
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
