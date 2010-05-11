/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: RemoteObjectHolder.java 
 *
 * @author qiying.wang [ May 11, 2010 | 4:32:47 PM ]
 *
 */
package com.hyk.rpc.core.remote;

import java.io.Serializable;

/**
 *
 */
public class RemoteObjectReference implements Serializable
{
	private long objID;
	private Object impl;
	
	
	private RemoteObjectReference()
	{
		
	}
	
	public Object getImpl()
	{
		return impl;
	}
	
	public long getObjID()
	{
		return objID;
	}
	
	public boolean isSerializable()
	{
		return impl instanceof Serializable;
	}
	
	public static RemoteObjectReference refernce(RemoteObjectProxy proxy, RemoteObjectFactory factory)
	{
		RemoteObjectReference reference = new RemoteObjectReference();
		reference.objID = proxy.getObjID();
		reference.impl = factory.getRawObject(reference.objID);
		return reference;
	}
}
