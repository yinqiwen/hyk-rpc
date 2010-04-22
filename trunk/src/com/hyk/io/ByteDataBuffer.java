/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: ChannelBuffer.java 
 *
 * @author qiying.wang [ Apr 22, 2010 | 11:13:48 AM ]
 *
 */
package com.hyk.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ByteDataBuffer
{
	private ByteBufferRepository repository = ByteBufferRepository.DefaultBuilder.buildDefaultRepository();
	
	private ByteBufferOutputStream out =  new ByteBufferOutputStream(repository);
	private ByteBufferInputStream in = new ByteBufferInputStream(repository);
	
	public static ByteDataBuffer allocate(int size)
	{
		return new ByteDataBuffer(size);
	}
	
	private ByteDataBuffer(int size)
	{
		
	}
	
	public ByteBufferInputStream getInputStream()
	{
		return in;
	}
	
	public ByteBufferOutputStream getOutputStream()
	{
		return out;
	}
	
	public void reset()
	{
		repository.reset();
	}
	
	public List<ByteBuffer> buffers()
	{
		return repository.takeAll();
	}

	public int size()
	{
		// TODO Auto-generated method stub
		return repository.peekAll().size();
	}
}
