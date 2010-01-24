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
import java.util.Arrays;

import com.hyk.serializer.Serializer;
import com.hyk.util.buffer.ByteArray;

/**
 *
 */
public class ProxySerializer<T> extends AbstractSerailizerImpl<T>
{

	@Override
	public T unmarshal(Class<T> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
	{
		try
		{	
			//AbstractSerailizerImpl<String[]> serializer = SerializerImplFactory.getSerializer(String[].class);
			//String[] interfaceNames = serializer.unmarshal(String[].class, data);
			String[] interfaceNames = readObject(data, String[].class);
			Class[] interfaces = new Class[interfaceNames.length];
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			for (int i = 0; i < interfaceNames.length; i++) {
				interfaces[i] = Class.forName(interfaceNames[i], true, loader);
			}
			Class proxyHandlerClass = Class.forName(readString(data), true, loader);
			InvocationHandler handler  = (InvocationHandler) readObject(data, proxyHandlerClass);
			//AbstractSerailizerImpl serializer2 = SerializerImplFactory.getSerializer(proxyHandlerClass);
			//InvocationHandler handler = (InvocationHandler) serializer2.unmarshal(proxyHandlerClass, data);
			return (T) Proxy.newProxyInstance(loader, interfaces, handler);
		}
		catch(Exception e)
		{
			throw new IOException(e);
		}
		
	}

	@Override
	public ByteArray marshal(T value, ByteArray data) throws NotSerializableException, IOException
	{
		Class[] interfaces = value.getClass().getInterfaces();
		String[] interfaceNames = new String[interfaces.length];
		for (int i = 0; i < interfaceNames.length; i++) {
			interfaceNames[i] = interfaces[i].getName();
		}
		writeObject(data, interfaceNames);
		InvocationHandler handler = Proxy.getInvocationHandler(value);
		writeString(data, handler.getClass().getName());
		writeObject(data,handler);
		return data;
	}

}
