/**
 * 
 */
package com.hyk.compress;

import java.io.IOException;

import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public interface Compressor {

//	public static final String SEVEN_ZIP = "7z";
//	public static final String ZIP = "zip";
//	public static final String GZIP = "gz";
	//public static final String BZIP2 = "bz2";
	
	
	//public byte[] compress(byte[] data, int offset, int length) throws IOException;
	//public byte[] compress(byte[] data) throws IOException;
	public CompressorType getType();
	public ByteArray compress(ByteArray data) throws IOException;
	public ByteArray decompress(ByteArray data) throws IOException;
//	
//	public byte[] decompress(byte[] data) throws IOException;
//	public byte[] decompress(byte[] data, int offset, int length) throws IOException;
}
