/**
 * 
 */
package com.hyk.protobuf.mapping.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;

/**
 * @author qiying.wang
 *
 */
public class DataType {
	
	private static Map<Class, String> reservedClassTable = new HashMap<Class, String>();
	
	static
	{
		reservedClassTable.put(int.class, "int32");
		reservedClassTable.put(Integer.class, "int32");
		reservedClassTable.put(short.class, "int32");
		reservedClassTable.put(Short.class, "int32");
		reservedClassTable.put(boolean.class, "bool");
		reservedClassTable.put(Boolean.class, "bool");
		reservedClassTable.put(float.class, "float");
		reservedClassTable.put(Float.class, "float");
		reservedClassTable.put(long.class, "int64");
		reservedClassTable.put(Long.class, "int64");
		reservedClassTable.put(String.class, "string");
		reservedClassTable.put(StringBuffer.class, "string");
		reservedClassTable.put(StringBuilder.class, "string");
		reservedClassTable.put(byte[].class, "bytes");
	}
	
	Class type;

	private DataType(Class type) {
		this.type = type;
	}
	
	public static DataType wrap(Class type)
	{
		return new DataType(type);
	}
	
	public boolean isReserved()
	{
		return reservedClassTable.containsKey(type);
	}
	
	public boolean isRequired()
	{
		return false;
	}
	
	public boolean isOptional()
	{
		return true;
	}
	
	public boolean isRepeated()
	{
		return type.isArray() || type.isAssignableFrom(List.class);
	}
	
	public String toString()
	{
		if(isReserved())
		{
			return reservedClassTable.get(type);
		}
		return type.getSimpleName();
	}
	
	
}
