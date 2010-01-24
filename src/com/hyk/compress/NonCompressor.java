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
public class NonCompressor extends AbstractCompressor {

	/* (non-Javadoc)
	 * @see com.hyk.compress.Compressor#compress(com.hyk.util.buffer.ByteArray)
	 */
	@Override
	public ByteArray compress(ByteArray data) throws IOException {
		return data;
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.Compressor#decompress(com.hyk.util.buffer.ByteArray)
	 */
	@Override
	public ByteArray decompress(ByteArray data) throws IOException {
		return data;
	}

}
