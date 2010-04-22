/**
 * 
 */
package com.hyk.compress.compressor.gz;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.ByteDataBuffer;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 * 
 */
public class GZipCompressor implements Compressor
{
	public static final String NAME = "gz";
	@Override
	public ByteDataBuffer compress(ByteDataBuffer data) throws IOException
	{
		ByteDataBuffer ret = ByteDataBuffer.allocate(data.size() / 3);
		GZIPOutputStream gos = new GZIPOutputStream(ret.getOutputStream());
		List<ByteBuffer> bufs = data.buffers();
		for(ByteBuffer buf:bufs)
		{
			byte[] raw = buf.array();
			int offset = buf.position();
			int len = buf.remaining();
			gos.write(raw, offset, len);
		}
		gos.flush();
		gos.finish();
		gos.close();
		return ret;
	}

	@Override
	public ByteDataBuffer decompress(ByteDataBuffer data) throws IOException
	{
		ByteDataBuffer ret = ByteDataBuffer.allocate(data.size() * 3);
		GZIPInputStream gis = new GZIPInputStream(data.getInputStream());
		int b;
		
		while((b = gis.read()) != -1)
		{
			//System.out.println("####" + b);
			ret.getOutputStream().write(b);
		}
		
		gis.close();
		//System.out.println("####@@@" + b);
		ret.flip();
		return ret;
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}

}
