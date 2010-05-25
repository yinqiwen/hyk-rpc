/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: OtherSerializer.java 
 *
 * @author qiying.wang [ Jan 21, 2010 | 3:07:12 PM ]
 *
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.hyk.io.buffer.ChannelDataBuffer;
import com.hyk.serializer.util.ObjectReferenceUtil;

/**
 *
 */
public class OtherSerializerStream extends SerailizerStream<Object>
{

	@Override
	public ChannelDataBuffer marshal(Object obj, ChannelDataBuffer data) throws NotSerializableException, IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
		oos.writeObject(obj);
		// don't close the stream
		return data;
	}

	@Override
	public Object unmarshal(Class<Object> type, ChannelDataBuffer data) throws NotSerializableException, IOException, InstantiationException
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
			Object ret = ois.readObject();
			ObjectReferenceUtil.addDeserializeThreadLocalObject(ret);
			// don't close the stream
			return ret;
		}
		catch(Exception e)
		{
			throw new IOException(e);
		}
	}

}
