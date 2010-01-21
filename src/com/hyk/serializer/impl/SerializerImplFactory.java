/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: SerializerImplFactory.java 
 *
 * @author qiying.wang [ Jan 21, 2010 | 11:25:13 AM ]
 *
 */
package com.hyk.serializer.impl;

import com.hyk.serializer.AbstractSerailizerImpl;
import com.hyk.serializer.Serializer;
import com.hyk.serializer.StandardSerializer;
import com.hyk.serializer.io.Type;
import com.hyk.serializer.reflect.ReflectionCache;

/**
 *
 */
public class SerializerImplFactory
{
	static IntSerializer intSerializer = new IntSerializer();
	static ShortSerializer shortSerializer = new ShortSerializer();
	static ByteSerializer byteSerializer = new ByteSerializer();
	static CharSerializer charSerializer = new CharSerializer();
	static BooleanSerializer boolSerializer = new BooleanSerializer();
	static LongSerializer longSerializer = new LongSerializer();
	static FloatSerializer floatSerializer = new FloatSerializer();
	static DoubleSerializer doubleSerializer = new DoubleSerializer();
	static StringSerializer stringSerializer = new StringSerializer();
	static EnumSerializer enumSerializer = new EnumSerializer();
	static ArraySerializer arraySerializer = new ArraySerializer();
	static ObjectSerializer objectSerializer = new ObjectSerializer();
	static ProxySerializer proxySerializer = new ProxySerializer();
	public static OtherSerializer otherSerializer = new OtherSerializer();
	
	public static  AbstractSerailizerImpl getSerializer(Class clazz)
	{
		Type type = ReflectionCache.getType(clazz);
		switch(type)
		{
			case BYTE:
			{
				return byteSerializer;
			}
			case CHAR:
			{
				return charSerializer;
			}
			case BOOL:
			{
				return boolSerializer;
			}
			case ARRAY:
			{
				return arraySerializer;
			}
			case DOUBLE:
			{
				return doubleSerializer;
			}
			case FLOAT:
			{
				return floatSerializer;
			}
			case INT:
			{
				return intSerializer;
			}
			case LONG:
			{
				return longSerializer;
			}
			case SHORT:
			{
				return shortSerializer;
			}
			case STRING:
			{
				return stringSerializer;
			}
			case POJO:
			{
				return objectSerializer;
			}
			case ENUM:
			{
				return enumSerializer;
			}
			case PROXY:
			{
				return proxySerializer;
			}
			case OTHER:
			{
				return otherSerializer;
			}
			
			default:
				return null;
		}
	}
}
