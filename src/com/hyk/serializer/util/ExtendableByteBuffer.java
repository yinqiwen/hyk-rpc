/**
 * 
 */
package com.hyk.serializer.util;

import java.nio.ByteBuffer;

/**
 * @author Administrator
 *
 */
public class ExtendableByteBuffer {

	private static final int DEFAULT_SIZE = 4096;
	private byte[] buffer;
	private int position;
	private int limit;
	
	public ExtendableByteBuffer()
	{
		buffer = new byte[DEFAULT_SIZE];
		position = 0;
		limit = DEFAULT_SIZE;
	}
	
	public ExtendableByteBuffer(int size)
	{
		buffer = new byte[size];
		position = 0;
		limit = size;
	}
	
	public ExtendableByteBuffer(byte[] buffer)
	{
		this.buffer = buffer;
		position = 0;
		limit = buffer.length;
	}
	
	public ExtendableByteBuffer(byte[] buffer, int off, int len)
	{
		this.buffer = buffer;
		position = off;
		limit = off + len;
	}
	
	public void put(byte b)
	{
		
	}
	
	protected void extendBuffer()
	{

	}
	
	public void put(byte[] src)
	{
		
	}
	
	public void put(byte[] src, int off, int len)
	{
		
	}
	
	public void flip()
	{
		
	}
	
}
