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
import com.hyk.util.buffer.ByteArray;

/**
 *
 */
public class QuickLZCompressor implements Compressor
{
	public static final String NAME = "quicklz";
	@Override
	public ByteArray compress(ByteArray data) throws IOException
	{
		byte[] raw = data.toByteArray();
		return ByteArray.wrap(QuickLZ.compress(raw, 1));
	}

	@Override
	public ByteArray decompress(ByteArray data) throws IOException
	{
		byte[] raw = data.toByteArray();
		return ByteArray.wrap(QuickLZ.decompress(raw));
	}

	@Override
	public String getName()
	{
		return NAME;
	}

}
