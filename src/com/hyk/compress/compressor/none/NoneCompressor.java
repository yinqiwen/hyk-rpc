/**
 * 
 */
package com.hyk.compress.compressor.none;

import java.io.IOException;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.ByteDataBuffer;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 * 
 */
public class NoneCompressor implements Compressor
{
	public static final String NAME = "none";
	@Override
	public ByteDataBuffer compress(ByteDataBuffer data) throws IOException
	{
		return data;
	}

	@Override
	public ByteDataBuffer decompress(ByteDataBuffer data) throws IOException
	{
		return data;
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
}
