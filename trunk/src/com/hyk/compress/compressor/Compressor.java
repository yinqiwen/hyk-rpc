/**
 * 
 */
package com.hyk.compress.compressor;

import java.io.IOException;

import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public interface Compressor {

	public String getName();
	public ByteArray compress(ByteArray data) throws IOException;
	public ByteArray decompress(ByteArray data) throws IOException;
}
