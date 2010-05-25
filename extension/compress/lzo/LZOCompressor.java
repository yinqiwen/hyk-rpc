/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: LZOCompressor.java 
 *
 * @author qiying.wang [ May 18, 2010 | 11:24:17 AM ]
 *
 */
package compress.lzo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.buffer.ChannelDataBuffer;

import compress.lzo.lzo.MiniLZO;
import compress.lzo.util.MInt;

/**
 *
 */
public class LZOCompressor implements Compressor
{
	int[] dict = new int[128*1024]; 
	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#compress(com.hyk.io.ByteDataBuffer)
	 */
	@Override
	public ChannelDataBuffer compress(ChannelDataBuffer data) throws IOException
	{
		Arrays.fill(dict, 0);
		byte[] input = ChannelDataBuffer.asByteArray(data);
		data.clear();
		MInt outL=new MInt();
		byte[] output = new byte[input.length + input.length / 16 + 64 + 3];
		MiniLZO.lzo1x_1_compress(input, input.length, output, outL, dict);
		ByteBuffer.wrap(output, 0, outL.v);
		return ChannelDataBuffer.wrap(output, 0, outL.v);
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#compress(com.hyk.io.ByteDataBuffer, com.hyk.io.ByteDataBuffer)
	 */
	@Override
	public ChannelDataBuffer compress(ChannelDataBuffer data, ChannelDataBuffer out) throws IOException
	{
		Arrays.fill(dict, 0);
		byte[] input = ChannelDataBuffer.asByteArray(data);
		data.clear();
		MInt outL=new MInt();
		byte[] output = new byte[input.length *3];
		MiniLZO.lzo1x_1_compress(input, input.length, output, outL, dict);
		ByteBuffer.wrap(output, 0, outL.v);
		return ChannelDataBuffer.wrap(output, 0, outL.v);
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#decompress(com.hyk.io.ByteDataBuffer)
	 */
	@Override
	public ChannelDataBuffer decompress(ChannelDataBuffer data) throws IOException
	{
		MInt OL=new MInt();
		byte[] input = ChannelDataBuffer.asByteArray(data);
		byte[] output = new byte[input.length *3];
		MiniLZO.lzo1x_decompress(input,input.length, output, OL);
		return ChannelDataBuffer.wrap(output, 0, OL.v);
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#getName()
	 */
	@Override
	public String getName()
	{
		return "lzo";
	}

}
