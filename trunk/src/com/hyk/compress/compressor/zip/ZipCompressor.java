/**
 * 
 */
package com.hyk.compress.compressor.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.ByteDataBuffer;

/**
 * @author Administrator
 * 
 */
public class ZipCompressor implements Compressor
{
	public static final String	NAME	= "zip";

	@Override
	public ByteDataBuffer compress(ByteDataBuffer data) throws IOException
	{
		ByteDataBuffer ret = ByteDataBuffer.allocate(data.size() / 3);

		// data.rewind();
		return compress(data, ret);
	}

	@Override
	public ByteDataBuffer decompress(ByteDataBuffer data) throws IOException
	{
		ByteDataBuffer ret = ByteDataBuffer.allocate(data.size() * 3);
		// ByteArrayInputStream bis = new ByteArrayInputStream(data, offset,
		// length);
		// ByteArrayOutputStream bos = new ByteArrayOutputStream(length * 3);
		ZipInputStream zis = new ZipInputStream(data.getInputStream());
		zis.getNextEntry();
		int b;

		while((b = zis.read()) != -1)
		{
			ret.getOutputStream().write(b);
		}
		zis.close();
		ret.flip();
		return ret;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public ByteDataBuffer compress(ByteDataBuffer data, ByteDataBuffer out) throws IOException
	{
		ZipOutputStream zos = new ZipOutputStream(out.getOutputStream());
		zos.putNextEntry(new ZipEntry("temp"));
		// zos.write(data.rawbuffer(), data.position(), data.size());
		List<ByteBuffer> bufs = data.buffers();
		for(ByteBuffer buf : bufs)
		{
			byte[] raw = buf.array();
			int offset = buf.position();
			int len = buf.limit() - buf.position();
			zos.write(raw, offset, len);
		}
		zos.flush();
		zos.closeEntry();
		zos.close();
		return out;
	}

}
