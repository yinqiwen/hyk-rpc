/**
 * 
 */
package com.hyk.compress;

import java.io.IOException;

import com.hyk.serializer.Externalizable;
import com.hyk.serializer.SerializerInput;
import com.hyk.serializer.SerializerOutput;

/**
 * @author Administrator
 *
 */
public class CompressorPreference implements Externalizable
{
	private int trigger = 256;
	private Compressor compressor = new NoneCompressor();

	public int getTrigger() 
	{
		return trigger;
	}
	public void setTrigger(int trigger) 
	{
		this.trigger = trigger;
	}
	public Compressor getCompressor() 
	{
		return compressor;
	}
	public void setCompressor(Compressor compressor) 
	{
		this.compressor = compressor;
	}
	
	@Override
	public void readExternal(SerializerInput in) throws IOException
	{
		trigger = in.readInt();
		compressor = CompressorFactory.getCompressor(CompressorType.valueOf(in.readInt()));
		
	}
	@Override
	public void writeExternal(SerializerOutput out) throws IOException
	{
		out.writeInt(trigger);
		out.writeInt(compressor.getType().getValue());	
	}
	
}
