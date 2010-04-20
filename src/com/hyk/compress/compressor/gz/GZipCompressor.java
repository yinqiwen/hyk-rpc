/**
 * 
 */
package com.hyk.compress.compressor.gz;

import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.hyk.compress.compressor.Compressor;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 * 
 */
public class GZipCompressor implements Compressor
{
	public static final String NAME = "gz";
	@Override
	public ByteArray compress(ByteArray data) throws IOException
	{
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		GZIPOutputStream gos = new GZIPOutputStream(ret.output);
		byte[] raw = data.rawbuffer();
		int offset = data.position();
		int len = data.size();
		gos.write(raw, offset, len);
		gos.finish();
		gos.close();
		return ret;
	}

	@Override
	public ByteArray decompress(ByteArray data) throws IOException
	{
		ByteArray ret = ByteArray.allocate(data.size() * 3);
		GZIPInputStream gis = new GZIPInputStream(data.input);
		int b;
		while((b = gis.read()) != -1)
		{
			ret.output.write(b);
		}
		gis.close();
		ret.flip();
		return ret;
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}

}
