/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: DefaultCompressPreference.java 
 *
 * @author yinqiwen [ 2010-3-28 | 09:35:02 PM ]
 *
 */
package com.hyk.compress;

/**
 *
 */
public class DefaultCompressPreference implements CompressPreference
{
	private static Compressor compressor;
	private static int trigger;
	
	public static void init(Compressor compressor, int trigger)
	{
		DefaultCompressPreference.compressor = compressor;
		DefaultCompressPreference.trigger = trigger;
	}
	
	@Override
	public Compressor getCompressor()
	{
		return compressor;
	}

	@Override
	public int getTrigger()
	{
		return trigger;
	}

}
