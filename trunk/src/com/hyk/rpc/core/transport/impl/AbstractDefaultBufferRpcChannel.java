/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: AbstractDefaultBufferRpcChannel.java 
 *
 * @author qiying.wang [ Apr 23, 2010 | 3:01:13 PM ]
 *
 */
package com.hyk.rpc.core.transport.impl;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.hyk.io.buffer.ChannelDataBuffer;
import com.hyk.rpc.core.address.Address;
import com.hyk.rpc.core.transport.RpcChannelData;

/**
 *
 */
public abstract class AbstractDefaultBufferRpcChannel extends AbstractDefaultRpcChannel
{
	protected static final int START_TAG = 0xCA;
	
	protected Address localAddr;
	
	protected BufferRpcChannelData readBuffer;
	protected BufferRpcChannelData writeBuffer;
	
	class BufferRpcChannelData
	{
		Address remoteAddress;
		ChannelDataBuffer data;
		
		int length = -1;
		public boolean isReady()
		{
			if(length < 0 || data.readableBytes() < length)
			{
				return false;
			}
			return true;
		}
		
		public int expectedRemaining()
		{
			return length - data.readableBytes();
		}
		
		public RpcChannelData get()
		{
			//RpcChannelData ret = new ;
			//data = null;
			return null;
		}
	}
	
	public AbstractDefaultBufferRpcChannel(Executor threadPool)
	{
		super(threadPool);
	}
	
	@Override
	public Address getRpcChannelAddress()
	{
		return localAddr;
	}
	
	private void addRawDataToReadBuffer(RpcChannelData raw)
	{
		
	}

	protected abstract RpcChannelData recvRawData() throws IOException;
	protected abstract void sendRawData(RpcChannelData data) throws IOException;
	
	@Override
	protected final RpcChannelData recv() throws IOException
	{
		while(!readBuffer.isReady())
		{
			addRawDataToReadBuffer(recvRawData());
		}
		return readBuffer.get();
	}

	
	@Override
	protected final void send(RpcChannelData data) throws IOException
	{
		//data.content

	}

}
