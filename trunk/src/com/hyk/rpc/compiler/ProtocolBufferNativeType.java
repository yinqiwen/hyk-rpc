/**
 * 
 */
package com.hyk.rpc.compiler;

/**
 * @author qiying.wang
 *
 */
public enum ProtocolBufferNativeType {
	DOUBLE("double"),
	FLOAT("float"),
	INT32("int32"),
	INT64("int64"),
	UINT32("uint32"),
	UINT64("uint64"),
	SINT32("sint32"),
	SINT64("sint64"),
	FIXED32("fixed32"),
	FIXED64("fixed64"),
	SFIXED32("sfixed32"),
	SFIXED64("sfixed64"),
	BOOL("bool"),
	STRING("string"),
	BYTES("bytes");
	
	private String code;
	
	private ProtocolBufferNativeType(String code)
	{
		this.code = code;
	}
	
	public String toString()
	{
		return code;
	}
	
	public static ProtocolBufferNativeType getNativeType(Class clazz)
	{
		
		return null;
	}
}
