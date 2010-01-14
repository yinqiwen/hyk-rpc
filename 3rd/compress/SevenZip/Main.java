/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010, BigBand Networks Inc. All rights reserved.
 *
 * Description: Main.java 
 *
 * @author qiying.wang [ Jan 14, 2010 | 5:37:08 PM ]
 *
 */
package SevenZip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
public class Main
{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		FileInputStream fis = new FileInputStream("nbbuild.xml");
		byte[] data = new byte[1024000];
		int len = fis.read(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
		ByteArrayInputStream bis = new ByteArrayInputStream(data, 0, len);
		SevenZip.Compression.LZMA.Encoder encoder = new SevenZip.Compression.LZMA.Encoder();
		encoder.WriteCoderProperties(bos);
		encoder.SetEndMarkerMode(false);
		long size;
			size = len;
		for (int i = 0; i < 8; i++)
			bos.write((int)(size >>> (8 * i)) & 0xFF);
		encoder.Code(bis, bos, -1, -1, null);
		
		byte[] compress = bos.toByteArray();
		System.out.println(len);
		System.out.println(compress.length);
		byte[] compare = Arrays.copyOf(data, len);
		
		SevenZip.Compression.LZMA.Decoder decoder = new SevenZip.Compression.LZMA.Decoder();
		ByteArrayInputStream inStream = new ByteArrayInputStream(compress);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int propertiesSize = 5;
		byte[] properties = new byte[propertiesSize];
		if (inStream.read(properties, 0, propertiesSize) != propertiesSize)
			throw new Exception("input .lzma file is too short");
		if (!decoder.SetDecoderProperties(properties))
			throw new Exception("Incorrect stream properties");
		long outSize = 0;
		for (int i = 0; i < 8; i++)
		{
			int v = inStream.read();
			if (v < 0)
				throw new Exception("Can't read stream size");
			outSize |= ((long)v) << (8 * i);
		}
		if (!decoder.Code(inStream, outStream, outSize))
			throw new Exception("Error in data stream");
		outStream.flush();
		outStream.close();
		inStream.close();
		byte[] compare1 = outStream.toByteArray();
		System.out.println(compare1.length);
		System.out.println(Arrays.equals(compare1, compare));
	}

}
