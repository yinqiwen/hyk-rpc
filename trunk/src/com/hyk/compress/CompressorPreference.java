/**
 * 
 */
package com.hyk.compress;

/**
 * @author Administrator
 *
 */
public class CompressorPreference 
{
	private boolean isEnable = false;
	private int trigger = 256;
	private Compressor compressor = new NoneCompressor();
	
	public boolean isEnable() 
	{
		return isEnable;
	}
	public void setEnable(boolean isEnable) 
	{
		this.isEnable = isEnable;
	}
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
	
}
