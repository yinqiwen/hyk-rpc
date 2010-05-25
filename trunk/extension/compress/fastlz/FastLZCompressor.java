/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: FastLZCompressor.java 
 *
 * @author qiying.wang [ May 18, 2010 | 11:11:25 AM ]
 *
 */
package compress.fastlz;

import java.io.IOException;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.buffer.ChannelDataBuffer;

/**
 *
 */
public class FastLZCompressor implements Compressor
{

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#compress(com.hyk.io.ByteDataBuffer)
	 */
	@Override
	public ChannelDataBuffer compress(ChannelDataBuffer data) throws IOException
	{
		ChannelDataBuffer ret = ChannelDataBuffer.allocate(1024);
		return compress(data, ret);
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#compress(com.hyk.io.ByteDataBuffer, com.hyk.io.ByteDataBuffer)
	 */
	@Override
	public ChannelDataBuffer compress(ChannelDataBuffer data, ChannelDataBuffer out) throws IOException
	{
		//ByteDataBuffer ret = ByteDataBuffer.allocate(1024);
		new JFastLZPack().packStream(data.getInputStream(), data.readableBytes(), "", out.getOutputStream(), JFastLZLevel.One);
		out.getOutputStream().close();
		data.clear();
		return out;
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#decompress(com.hyk.io.ByteDataBuffer)
	 */
	@Override
	public ChannelDataBuffer decompress(ChannelDataBuffer data) throws IOException
	{
		ChannelDataBuffer ret = ChannelDataBuffer.allocate(1024);
		new JFastLZUnpack().unpackStream(data.getInputStream(), ret.getOutputStream());
		ret.getOutputStream().close();
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.hyk.compress.compressor.Compressor#getName()
	 */
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "fastlz";
	}

}
