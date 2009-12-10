/**
 * 
 */
package com.hyk.protobuf.mapping.data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author qiying.wang
 *
 */
public class MessageInfo {
	
	public DataType type;
	
	public List<FieldInfo> fields = new LinkedList<FieldInfo>();
	
	public String getName()
	{
		return type.toString();
	}
	
	public String getPackageName()
	{
		return type.type.getPackage().getName();
	}
	
}
