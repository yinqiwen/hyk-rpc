/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: ObjectReferenceUtil.java 
 *
 * @author qiying.wang [ Jan 28, 2010 | 11:28:15 AM ]
 *
 */
package com.hyk.serializer.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ObjectReferenceUtil
{
	private static ThreadLocal<List<Object>>	serializeRferenceTable	= new ThreadLocal<List<Object>>()
																		{
																			protected List<Object> initialValue()
																			{
																				return new ArrayList<Object>();
																			}
																		};
	private static ThreadLocal<List<Object>>	deserializeRferenceTable	= new ThreadLocal<List<Object>>()
																		{
																			protected List<Object> initialValue()
																			{
																				return new ArrayList<Object>();
																			}
																		};

	public static void addSerializeThreadLocalObject(Object obj)
	{
		serializeRferenceTable.get().add(obj);
	}

	public static Object getSerializeThreadLocalObject(int seq)
	{
		return serializeRferenceTable.get().get(seq);
	}

	public static int querySerializeThreadLocalObjectIndex(Object obj)
	{
		List<Object> list = serializeRferenceTable.get();
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) == obj)
			{
				return i;
			}
		}
		return -1;
	}
	
	public static void cleanSerializeThreadLocalObjects()
	{
		serializeRferenceTable.get().clear();
	}
	
	public static void addDeserializeThreadLocalObject(Object obj)
	{
		deserializeRferenceTable.get().add(obj);
	}

	public static Object getDeserializeThreadLocalObject(int seq)
	{
		return deserializeRferenceTable.get().get(seq);
	}

	public static int queryDeserializeThreadLocalObjectIndex(Object obj)
	{
		List<Object> list = deserializeRferenceTable.get();
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) == obj)
			{
				return i;
			}
		}
		return -1;
	}
	
	public static void cleanDeserializeThreadLocalObjects()
	{
		deserializeRferenceTable.get().clear();
	}
}
