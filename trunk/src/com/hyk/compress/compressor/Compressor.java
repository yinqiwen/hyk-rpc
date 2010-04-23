/**
 * 
 */
package com.hyk.compress.compressor;

import java.io.IOException;

import com.hyk.io.ByteDataBuffer;

/**
 * @author Administrator
 *
 */
public interface Compressor {

	public String getName();
	public ByteDataBuffer compress(ByteDataBuffer data) throws IOException;
	public ByteDataBuffer compress(ByteDataBuffer data, ByteDataBuffer out) throws IOException;
	public ByteDataBuffer decompress(ByteDataBuffer data) throws IOException;
}
