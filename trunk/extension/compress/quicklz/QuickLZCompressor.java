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
import com.hyk.io.buffer.ChannelDataBuffer;

/**
 *
 */
public class QuickLZCompressor implements Compressor
{
	public static final String NAME = "quicklz";
	@Override
	public ChannelDataBuffer compress(ChannelDataBuffer data) throws IOException
	{
		byte[] raw = ChannelDataBuffer.asByteArray(data);
		data.clear();
		return ChannelDataBuffer.wrap(QuickLZ.compress(raw, 1));
	}

	@Override
	public ChannelDataBuffer decompress(ChannelDataBuffer data) throws IOException
	{
		byte[] raw = ChannelDataBuffer.asByteArray(data);
		return ChannelDataBuffer.wrap(QuickLZ.decompress(raw));
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public ChannelDataBuffer compress(ChannelDataBuffer data, ChannelDataBuffer out) throws IOException
	{
		byte[] raw = ChannelDataBuffer.asByteArray(data);
		out.writeBytes(QuickLZ.compress(raw, 1));
		out.flip();
		return out;
	}

}
