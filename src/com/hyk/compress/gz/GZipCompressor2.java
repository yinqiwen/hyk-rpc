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
public class GZipCompressor2 extends AbstractCompressor {

	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#compress(byte[], int, int)
	 */
	@Override
	public ByteArray compress(ByteArray data)
			throws IOException {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(length / 3);
//		GZIPOutputStream gos = new GZIPOutputStream(bos);
//		gos.write(data, offset, length);
//		gos.finish();
//		gos.flush();
//		gos.close();
//		return bos.toByteArray();
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		GZIPOutputStream gos = new GZIPOutputStream(ret.output);
		byte[] raw = data.rawbuffer();
		int offset = data.position();
		int len = data.size();
		gos.write(raw, offset, len);
		gos.finish();
		gos.flush();
		gos.close();
		System.out.println("###1 " + ret.size());
		return ret;
	}


	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#decompress(byte[], int, int)
	 */
	@Override
	public ByteArray decompress(ByteArray data)
			throws IOException {
		System.out.println("###2 " + data.rawbuffer()[data.position()]);
//		ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(length * 3);
//		GZIPInputStream gis = new GZIPInputStream(bis);
//		byte b;
//		while((b = (byte) gis.read()) != -1)
//		{
//			bos.write(b);
//		}
//		return bos.toByteArray();
		ByteArray ret = ByteArray.allocate(data.size() * 3);
		byte[] ba = data.rawbuffer();
		ByteArrayInputStream bis = new ByteArrayInputStream(ba, 0, data.size());
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
