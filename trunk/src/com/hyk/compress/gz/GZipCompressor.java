/**
 * 
 */
package com.hyk.compress.gz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.hyk.compress.AbstractCompressor;
import com.hyk.compress.Compressor;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class GZipCompressor extends AbstractCompressor {

	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#compress(byte[], int, int)
	 */
	@Override
	public ByteArray compress(ByteArray data)
			throws IOException {
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		GZIPOutputStream gos = new GZIPOutputStream(ret.output);
		byte[] raw = data.rawbuffer();
		int offset = data.position();
		int len = data.size();
		gos.write(raw, offset, len);
		gos.finish();
		gos.flush();
		gos.close();
		//System.out.println("###1 " + ret.size());
		//data.rewind();
		return ret;
	}

	@Override
	public ByteArray decompress(ByteArray data)
			throws IOException {
		ByteArray ret = ByteArray.allocate(data.size() * 3);
		GZIPInputStream gis = new GZIPInputStream(data.input);
		byte b;
		while((b = (byte) gis.read()) != -1)
		{
			ret.output.write(b);
		}
		gis.close();
		ret.flip();
		return ret;
	}

}
