/**
 * 
 */
package com.hyk.protobuf.mapping.data;

import java.util.List;

/**
 * @author qiying.wang
 *
 */
public class FieldInfo {

	public String name;
	public int tag;
	public DataType type;
	
	public boolean isRequired()
	{
		return type.isRequired();
	}
	
	public boolean isOptional()
	{
		return type.isOptional();
	}
	
	public boolean isRepeated()
	{
		return type.isRepeated();
	}
}
