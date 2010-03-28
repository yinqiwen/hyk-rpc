/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: EmptyCompressPreference.java 
 *
 * @author yinqiwen [ 2010-3-28 | 09:40:10 PM ]
 *
 */
package com.hyk.compress;

/**
 *
 */
public class EmptyCompressPreference implements CompressPreference
{
	Compressor compressor = new NoneCompressor();
	@Override
	public Compressor getCompressor()
	{
		return compressor;
	}

	@Override
	public int getTrigger()
	{
		return 0;
	}

}
