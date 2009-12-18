/**
 * 
 */
package com.hyk.rpc.core.message;

/**
 * @author qiying.wang
 *
 */
public enum MessageType {

	Unknown(-1),Request(0), Response(1), Exception(2);
	
	private final int value;
	private MessageType(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public static MessageType valueOf(int value)
	{
		if(value >= 0 && value <= 2)
		{
		    return values()[value];
		}
		return Unknown;
	}
}
