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

/**
 * @author Administrator
 *
 */
public class GZipCompressor {


	public byte[] compress(byte[] data, int offset, int length)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length / 3);
		GZIPOutputStream gos = new GZIPOutputStream(bos);
		gos.write(data, offset, length);
		gos.finish();
		gos.flush();
		gos.close();
		byte[] ret =  bos.toByteArray();
		System.out.println("???1 " + ret.length);
		return ret;
	}


	
	public byte[] decompress(byte[] data, int offset, int length)
			throws IOException {
		System.out.println("???2 " + data[offset]);
		ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length * 3);
		GZIPInputStream gis = new GZIPInputStream(bis);
		byte b;
		while((b = (byte) gis.read()) != -1)
		{
			bos.write(b);
		}
		return bos.toByteArray();
	}

}
