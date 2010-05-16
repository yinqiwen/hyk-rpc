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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 */
public class ByteDataBuffer
{
	private ByteBufferRepository	repository	= ByteBufferRepository.DefaultBuilder.buildDefaultRepository();

	private final ByteBufferOutputStream	out			;
	private final ByteBufferInputStream	in			= new ByteBufferInputStream(repository);

	public static ByteDataBuffer wrap(byte[] b)
	{
		return new ByteDataBuffer(ByteBuffer.wrap(b));
	}

	public static ByteDataBuffer wrap(ByteBuffer buffer)
	{
		return new ByteDataBuffer(buffer);
	}

	public static ByteDataBuffer allocate(int size)
	{
		return new ByteDataBuffer(size);
	}

	private ByteDataBuffer(int size)
	{
		out = new ByteBufferOutputStream(repository, size);
	}

	private ByteDataBuffer(ByteBuffer buffer)
	{
		repository.put(buffer);
		out = new ByteBufferOutputStream(repository);
		//in = 
	}

	public ByteBufferInputStream getInputStream()
	{
		return in;
	}

	public ByteBufferOutputStream getOutputStream()
	{
		return out;
	}

	public void reset() throws IOException
	{
		repository.reset();
		in.reset();
	}

	public ByteBuffer[] buffers()
	{
		List<ByteBuffer> ret = repository.takeAll();
		ByteBuffer[] array = new ByteBuffer[ret.size()];
		return ret.toArray(array);
		//return repository.takeAll();
	}

	public int size()
	{
		List<ByteBuffer> bufs = repository.peekAll();
		int size = 0;
		for(ByteBuffer buf : bufs)
		{
			size += buf.remaining();
		}
		return size;
	}

	public void flip()
	{
		try
		{
			out.flush();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void put(byte[] data)
	{
		try
		{
			out.write(data);
		}
		catch(Exception e)
		{

		}

	}

	public void put(ByteBuffer data)
	{
		try
		{
			out.write(data);
		}
		catch(Exception e)
		{

		}

	}

	public void put(ByteDataBuffer data)
	{
		try
		{
			List<ByteBuffer> all = data.repository.takeAll();
			for(ByteBuffer buf : all)
			{
				out.write(buf);
			}
		}
		catch(Exception e)
		{

		}

	}

	public void get(byte[] b)
	{
		try
		{
			in.read(b);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

//	public void get(ByteBuffer b)
//	{
//		try
//		{
//			in.read(b);
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
//	}

	public ByteBuffer toByteBuffer()
	{
		repository.reset();
		if(repository.size() == 1)
		{
			return repository.peek();
		}
		byte[] buffer = new byte[size()];
		get(buffer);
		return ByteBuffer.wrap(buffer);
	}

	public boolean equals(Object obj)
	{
		if(null == obj)
		{
			return false;
		}
		if(obj instanceof ByteDataBuffer)
		{
			ByteDataBuffer other = (ByteDataBuffer)obj;
			if(size() == other.size())
			{
				ByteBufferInputStream ois = other.in;
				try
				{
					int i = 0;
					int size = size();
					while(i < size)
					{
						try
						{
							if(in.read() != ois.read())
							{
								return false;
							}
							i++;
						}
						catch(IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return true;
				}
				finally
				{
					try
					{
						reset();
						other.reset();
					}
					catch(IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
		return false;
	}

	public byte[] toByteArray()
	{
		byte[] ret = new byte[size()];
		try
		{
			in.read(ret);
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

}
