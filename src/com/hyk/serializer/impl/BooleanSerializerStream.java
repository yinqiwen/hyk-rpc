/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: BooleanSerializer.java 
 *
 * @author qiying.wang [ Jan 21, 2010 | 11:29:34 AM ]
 *
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.util.buffer.ByteArray;

/**
 *
 */
public class BooleanSerializerStream extends SerailizerStream<Boolean>
{

	@Override
	protected Boolean unmarshal(Class<Boolean> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
	{
		return readBool(data);
	}

	@Override
	protected ByteArray marshal(Boolean value, ByteArray data) throws NotSerializableException, IOException
	{
		writeBoolean(data, value);
		return data;
	}

}
