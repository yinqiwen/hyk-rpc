package compress.lzf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import com.hyk.compress.compressor.Compressor;
import com.hyk.io.ByteDataBuffer;

import com.hyk.util.buffer.ByteArray;

public class LZFCompressor implements Compressor {

	@Override
	public ByteDataBuffer compress(ByteDataBuffer data)
			throws IOException {
		ByteDataBuffer ret = ByteDataBuffer.allocate(data.size() / 3);
		LZFOutputStream lzfos = new LZFOutputStream(ret.getOutputStream());
		List<ByteBuffer> bufs = data.buffers();
		for(ByteBuffer buf:bufs)
		{
			byte[] raw = buf.array();
			int offset = buf.position();
			int len = buf.remaining();
			lzfos.write(raw, offset, len);
		}
		
		lzfos.close();
		return ret;
	}

	@Override
	public ByteDataBuffer decompress(ByteDataBuffer data)
			throws IOException {
		ByteDataBuffer ret = ByteDataBuffer.allocate(data.size() * 3);
		LZFInputStream lzfis = new LZFInputStream(data.getInputStream());
		int b;
		while((b = lzfis.read()) != -1)
		{
			ret.getOutputStream().write(b);
		}
		lzfis.close();
		ret.flip();
		return ret;
	}

	@Override
	public String getName()
	{
		return "lzf";
	}

}

