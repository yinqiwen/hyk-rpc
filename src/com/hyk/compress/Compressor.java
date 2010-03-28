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

	public CompressorType getType();
	public ByteArray compress(ByteArray data) throws IOException;
	public ByteArray decompress(ByteArray data) throws IOException;
}
