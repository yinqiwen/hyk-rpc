/**
 * This file is part of the hyk-proxy project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: QuickLZCompressor.java 
 *
 * @author qiying.wang [ Apr 20, 2010 | 3:10:22 PM ]
 *
 */
package compress.quicklz;

import java.io.IOException;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.ByteDataBuffer;
import com.hyk.util.buffer.ByteArray;

/**
 *
 */
public class QuickLZCompressor implements Compressor
{
	public static final String NAME = "quicklz";
	@Override
	public ByteDataBuffer compress(ByteDataBuffer data) throws IOException
	{
		byte[] raw = data.toByteArray();
		return ByteDataBuffer.wrap(QuickLZ.compress(raw, 1));
	}

	@Override
	public ByteDataBuffer decompress(ByteDataBuffer data) throws IOException
	{
		byte[] raw = data.toByteArray();
		return ByteDataBuffer.wrap(QuickLZ.decompress(raw));
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public ByteDataBuffer compress(ByteDataBuffer data, ByteDataBuffer out) throws IOException
	{
		byte[] raw = data.toByteArray();
		out.put(QuickLZ.compress(raw, 1));
		out.flip();
		return out;
	}

}
