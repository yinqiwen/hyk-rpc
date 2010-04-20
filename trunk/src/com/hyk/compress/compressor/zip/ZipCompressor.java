/**
 * 
 */
package com.hyk.compress.compressor.zip;

import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.hyk.compress.compressor.Compressor;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 * 
 */
public class ZipCompressor implements Compressor
{
	public static final String NAME = "zip";
	@Override
	public ByteArray compress(ByteArray data) throws IOException
	{
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		ZipOutputStream zos = new ZipOutputStream(ret.output);
		zos.putNextEntry(new ZipEntry("temp"));
		zos.write(data.rawbuffer(), data.position(), data.size());
		zos.flush();
		zos.closeEntry();
		zos.close();
		data.rewind();
		return ret;
	}

	@Override
	public ByteArray decompress(ByteArray data) throws IOException
	{
		ByteArray ret = ByteArray.allocate(data.size() * 3);
		// ByteArrayInputStream bis = new ByteArrayInputStream(data, offset,
		// length);
		// ByteArrayOutputStream bos = new ByteArrayOutputStream(length * 3);
		ZipInputStream zis = new ZipInputStream(data.input);
		zis.getNextEntry();
		int b;

		while((b = zis.read()) != -1)
		{
			ret.output.write(b);
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

}
