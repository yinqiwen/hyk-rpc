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
public class NoneCompressor extends AbstractCompressor {

	public CompressorType getType()
	{
		return CompressorType.NONE;
	}
	
	@Override
	public ByteArray compress(ByteArray data) throws IOException {
		return data;
	}

	@Override
	public ByteArray decompress(ByteArray data) throws IOException {
		return data;
	}

}
