/**
 * 
 */
package com.hyk.compress.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.hyk.compress.AbstractCompressor;
import com.hyk.compress.Compressor;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class ZipCompressor extends AbstractCompressor {

	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#compress(byte[], int, int)
	 */
	@Override
	public ByteArray compress(ByteArray data)
			throws IOException {
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream(length / 3);
		ZipOutputStream zos = new ZipOutputStream(ret.output);
		//zos.setLevel(9);
		zos.putNextEntry(new ZipEntry("temp"));
		zos.write(data.rawbuffer(), data.position(), data.size());
		zos.flush();
		zos.closeEntry();
		zos.close();
		//System.out.println("###" + ret.position());
		//ret.flip();
		data.rewind();
		//System.out.println("###" + ret.size());
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
		ZipInputStream zis = new ZipInputStream(data.input);
		zis.getNextEntry();
		byte b;
		
		while((b = (byte) zis.read()) != -1)
		{
			//System.out.println("###");
			//bos.write(b);
			ret.output.write(b);
		}
		zis.close();
		ret.flip();
		return ret;
	}

}
