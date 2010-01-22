/**
 * 
 */
package com.hyk.compress;

import java.util.HashMap;
import java.util.Map;

//import com.hyk.compress.bz2.BZip2Compressor;
import com.hyk.compress.gz.GZipCompressor;
import com.hyk.compress.sevenzip.SevenZipCompressor;
import com.hyk.compress.zip.ZipCompressor;

/**
 * @author Administrator
 *
 */
public class CompressorFactory {

	private static Map<String, Class> compressorClassTable = new HashMap<String, Class>();
	
	static
	{
		compressorClassTable.put(Compressor.SEVEN_ZIP, SevenZipCompressor.class);
		compressorClassTable.put(Compressor.GZIP, GZipCompressor.class);
		compressorClassTable.put(Compressor.ZIP, ZipCompressor.class);
		//compressorClassTable.put(Compressor.BZIP2, BZip2Compressor.class);
	}

	public static Compressor getCompressor(String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class clazz = compressorClassTable.get(name);
		if(null == clazz)
		{
			clazz = GZipCompressor.class;
		}
		return (Compressor) clazz.newInstance();
	}
	
}
