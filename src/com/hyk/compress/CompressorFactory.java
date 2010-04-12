/**
 * 
 */
package com.hyk.compress;

import com.hyk.compress.gz.GZipCompressor;
import com.hyk.compress.zip.ZipCompressor;

/**
 * @author Administrator
 * 
 */
public class CompressorFactory
{

	public static Compressor getCompressor(CompressorType type)
	{
		switch(type)
		{
			case ZIP:
			{
				return new ZipCompressor();
			}
			case GZ:
			{
				return new GZipCompressor();
			}
			default:
			{
				return new NoneCompressor();
			}
		}
	}

	public static Compressor getCompressor(String name)
	{
		CompressorType type = Enum.valueOf(CompressorType.class, name);
		return getCompressor(type);
	}

}
