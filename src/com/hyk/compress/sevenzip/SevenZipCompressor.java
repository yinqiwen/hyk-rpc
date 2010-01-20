package com.hyk.compress.sevenzip;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.hyk.compress.AbstractCompressor;
import com.hyk.compress.Compressor;

/**
 * @author Administrator
 *
 */
public class SevenZipCompressor extends AbstractCompressor {

	private static com.hyk.compress.sevenzip.Compression.LZMA.Decoder decoder = new com.hyk.compress.sevenzip.Compression.LZMA.Decoder();
	private static com.hyk.compress.sevenzip.Compression.LZMA.Encoder encoder = new com.hyk.compress.sevenzip.Compression.LZMA.Encoder();
	@Override
	public byte[] compress(byte[] data, int offset, int length) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length / 3);
		ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
		
		encoder.WriteCoderProperties(bos);
		encoder.SetEndMarkerMode(false);
		long size = data.length;
		for (int i = 0; i < 8; i++)
			bos.write((int)(size >>> (8 * i)) & 0xFF);
		encoder.Code(bis, bos, -1, -1, null);
		return bos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compresser#decompress(byte[])
	 */
	@Override
	public byte[] decompress(byte[] data, int offset, int length) throws IOException{
		
		ByteArrayInputStream inStream = new ByteArrayInputStream(data, offset, length);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(length*3);
		int propertiesSize = 5;
		byte[] properties = new byte[propertiesSize];
		if (inStream.read(properties, 0, propertiesSize) != propertiesSize)
			throw new IOException("input .lzma file is too short");
		if (!decoder.SetDecoderProperties(properties))
			throw new IOException("Incorrect stream properties");
		long outSize = 0;
		for (int i = 0; i < 8; i++)
		{
			int v = inStream.read();
			if (v < 0)
				throw new IOException("Can't read stream size");
			outSize |= ((long)v) << (8 * i);
		}
		if (!decoder.Code(inStream, outStream, outSize))
			throw new IOException("Error in data stream");
		outStream.flush();
		return outStream.toByteArray();
	}

	

}
