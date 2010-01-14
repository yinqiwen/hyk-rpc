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

/**
 * @author Administrator
 *
 */
public class ZipCompressor extends AbstractCompressor {

	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#compress(byte[], int, int)
	 */
	@Override
	public byte[] compress(byte[] data, int offset, int length)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length / 3);
		ZipOutputStream zos = new ZipOutputStream(bos);
		//zos.setLevel(9);
		zos.putNextEntry(new ZipEntry("temp"));
		zos.write(data, offset, length);
		zos.flush();
		zos.closeEntry();
		zos.close();
		return bos.toByteArray();
	}


	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compressor#decompress(byte[], int, int)
	 */
	@Override
	public byte[] decompress(byte[] data, int offset, int length)
			throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length * 3);
		ZipInputStream zis = new ZipInputStream(bis);
		zis.getNextEntry();
		byte b;
		//System.out.println("###" + length);
		while((b = (byte) zis.read()) != -1)
		{
			//System.out.println("###");
			bos.write(b);
		}
		return bos.toByteArray();
	}

}
