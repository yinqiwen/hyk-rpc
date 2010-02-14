/**
 * 
 */
package com.hyk.compress.bz2;

import java.io.IOException;

import com.hyk.compress.AbstractCompressor;
import com.hyk.compress.CompressorType;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class BZ2Compressor extends AbstractCompressor {

	public CompressorType getType()
	{
		return CompressorType.BZ2;
	}

	@Override
	public ByteArray compress(ByteArray data)
			throws IOException {
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream(length / 3);
		CBZip2OutputStream zos = new CBZip2OutputStream(ret.output);
		zos.write(data.rawbuffer(), data.position(), data.size());
		zos.flush();
		zos.close();
		data.rewind();
		return ret;
	}


	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#decompress(byte[], int, int)
	 */
	@Override
	public ByteArray decompress(ByteArray data)
			throws IOException {
		ByteArray ret = ByteArray.allocate(data.size() * 3);
		//ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream(length * 3);
		CBZip2InputStream zis = new CBZip2InputStream(data.input);
		int b;
		
		while((b = zis.read()) != -1)
		{
			ret.output.write(b);
		}
		zis.close();
		ret.flip();
		return ret;
	}

}
