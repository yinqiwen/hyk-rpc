/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: ProxySerializer.java 
 *
 * @author qiying.wang [ Jan 21, 2010 | 1:50:36 PM ]
 *
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.hyk.serializer.AbstractSerailizerImpl;
import com.hyk.serializer.Serializer;
import com.hyk.util.buffer.ByteArray;

/**
 *
 */
public class ProxySerializer<T> extends AbstractSerailizerImpl<T>
{

	@Override
	protected T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
	{
		try
		{	
			Serializer serializer = SerializerImplFactory.getSerializer(String[].class);
			String[] interfaceNames = serializer.deserialize(String[].class, data);
			Class[] interfaces = new Class[interfaceNames.length];
			ClassLoader loader = type.getClassLoader();
			for (int i = 0; i < interfaceNames.length; i++) {
				interfaces[i] = Class.forName(interfaceNames[i], true, loader);
			}
			Class proxyHandlerClass = Class.forName(readString(data), true, loader);
			Serializer serializer2 = SerializerImplFactory.getSerializer(proxyHandlerClass);
			InvocationHandler handler = (InvocationHandler) serializer2.deserialize(proxyHandlerClass, data);
			return (T) Proxy.newProxyInstance(loader, interfaces, handler);
		}
		catch(Exception e)
		{
			throw new IOException(e);
		}
		
	}

	@Override
	protected ByteArray marshal(T value, ByteArray data) throws NotSerializableException, IOException
	{
		Serializer serializer = SerializerImplFactory.getSerializer(String[].class);
		Class[] interfaces = value.getClass().getInterfaces();
		//System.out.println("####" + interfaces.length);
		String[] interfaceNames = new String[interfaces.length];
		for (int i = 0; i < interfaceNames.length; i++) {
			interfaceNames[i] = interfaces[i].getName();
		}
		serializer.serialize(interfaceNames, data);
		InvocationHandler handler = Proxy.getInvocationHandler(value);
		writeString(data, handler.getClass().getName());
		Serializer serializer2 = SerializerImplFactory.getSerializer(handler.getClass());
		serializer2.serialize(handler, data);
		return data;
	}

}
