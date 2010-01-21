package com.hyk.compress.sevenzip;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.hyk.compress.AbstractCompressor;
import com.hyk.compress.Compressor;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class SevenZipCompressor extends AbstractCompressor {

	private static com.hyk.compress.sevenzip.Compression.LZMA.Decoder decoder = new com.hyk.compress.sevenzip.Compression.LZMA.Decoder();
	private static com.hyk.compress.sevenzip.Compression.LZMA.Encoder encoder = new com.hyk.compress.sevenzip.Compression.LZMA.Encoder();
	@Override
	public ByteArray compress(ByteArray data) throws IOException {
		ByteArray ret = ByteArray.allocate(data.size() / 3);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream(data.size() / 3);
		//ByteArrayInputStream bis = new ByteArrayInputStream(data, offset, length);
		
		encoder.WriteCoderProperties(ret.output);
		encoder.SetEndMarkerMode(false);
		long size = data.size();
		
		for (int i = 0; i < 8; i++)
			ret.output.write((int)(size >>> (8 * i)) & 0xFF);
		encoder.Code(ret.input, ret.output, -1, -1, null);
		ret.flip();
		data.rewind();
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.hyk.serializer.compress.Compresser#decompress(byte[])
	 */
	@Override
	public ByteArray decompress(ByteArray data) throws IOException{
		ByteArray ret = ByteArray.allocate(data.size() * 3);
		//ByteArrayInputStream inStream = new ByteArrayInputStream(data, offset, length);
		//ByteArrayOutputStream outStream = new ByteArrayOutputStream(length*3);
		int propertiesSize = 5;
		byte[] properties = new byte[propertiesSize];
		if (data.input.read(properties, 0, propertiesSize) != propertiesSize)
			throw new IOException("input .lzma file is too short");
		if (!decoder.SetDecoderProperties(properties))
			throw new IOException("Incorrect stream properties");
		long outSize = 0;
		for (int i = 0; i < 8; i++)
		{
			int v = data.input.read();
			if (v < 0)
				throw new IOException("Can't read stream size");
			outSize |= ((long)v) << (8 * i);
		}
		if (!decoder.Code(data.input, ret.output, outSize))
			throw new IOException("Error in data stream");
		//ret.output.flush();
		ret.flip();
		data.rewind();
		return ret;
	}

	

}
