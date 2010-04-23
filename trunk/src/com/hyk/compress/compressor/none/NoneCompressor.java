/**
 * 
 */
package com.hyk.compress.compressor.none;

import java.io.IOException;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.ByteDataBuffer;

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

	@Override
	public ByteDataBuffer compress(ByteDataBuffer data, ByteDataBuffer out) throws IOException
	{
		return out;
	}
}
