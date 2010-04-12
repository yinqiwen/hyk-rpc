/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: CompressorType.java 
 *
 * @author Administrator [ 2010-2-5 | pm08:00:49 ]
 *
 */
package com.hyk.compress;

/**
 *
 */
public enum CompressorType
{
	NONE("none", 0),BZ2("bz2", 1), ZIP("zip", 2), GZ("gz", 3) ;
	
	private String name;
	private int value;
	
	private CompressorType(String name, int value)
	{
		this.name = name;
		this.value = value;
	}
	
	public final int getValue() {
		return value;
	}
	
	public static CompressorType valueOf(int value)
	{
		return values()[value];
	}
	
	public static CompressorType valueOfName(String name)
	{
		CompressorType[] values = values();
		for(CompressorType v:values)
		{
			if(v.name.equals(name))
			{
				return v;
			}
		}
		return NONE;
	}
	
	public String toString()
	{
		return name;
	}
}
