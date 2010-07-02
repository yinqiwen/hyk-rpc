package com.hyk.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import junit.framework.TestCase;

import com.hyk.compress.compressor.Compressor;
import com.hyk.compress.compressor.gz.GZipCompressor;
import com.hyk.compress.compressor.zip.ZipCompressor;
import com.hyk.io.buffer.ChannelDataBuffer;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.serializer.HykSerializer;
import compress.fastlz.FastLZCompressor;
import compress.lzf.LZFCompressor;
import compress.lzo.LZOCompressor;
import compress.quicklz.QuickLZCompressor;

public class CompressorTest extends TestCase
{

	ChannelDataBuffer	orginal;
	byte[]		orginal1;
	Compressor	compressor;
	int			repeat	= 1;

	protected void setUp() throws Exception
	{
		super.setUp();
		File f = new File("build.xml");
		long size = f.length();
		byte[] orginalb = new byte[(int)size];
		FileInputStream fis = new FileInputStream(f);
		fis.read(orginalb);
		orginal1 = orginalb;
		fis.close();
		orginal = ChannelDataBuffer.wrap(orginalb);
	}

	// public void testBZ2() throws IOException {
	// compressor = new BZ2Compressor();
	// ByteArray compressed = compressor.compress(orginal);
	// ByteArray restore = compressor.decompress(compressed);
	// assertEquals(orginal, restore);
	// assertTrue(restore.size() > (compressed.size() * 2));
	// }

	public void _testZip() throws IOException
	{
		compressor = new ZipCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ChannelDataBuffer compressed = compressor.compress(orginal);
			
			//System.out.println("#####Compress:" + compressed.size());
			ChannelDataBuffer restore = compressor.decompress(compressed);
			
			//assertEquals(orginal, restore);
			//System.out.println("#####Original:" + restore.size());
			assertEquals(orginal.readableBytes(), restore.readableBytes());
			
			
			assertTrue(restore.readableBytes() > (compressed.readableBytes() * 2));
		}

	}

	public void testQuickLZ() throws IOException
	{
		compressor = new QuickLZCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ChannelDataBuffer compressed = compressor.compress(orginal);
			ChannelDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			assertEquals(orginal.readableBytes(), restore.readableBytes());
			assertTrue(restore.readableBytes() > (compressed.readableBytes() * 2));
			//assertEquals(orginal, restore);
		}
	}
	
	public void testFastLZ() throws IOException
	{
		compressor = new FastLZCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ChannelDataBuffer compressed = compressor.compress(orginal);
			ChannelDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			assertEquals(orginal.readableBytes(), restore.readableBytes());
			assertTrue(restore.readableBytes() > (compressed.readableBytes() * 2));
			//assertEquals(orginal, restore);
		}
	}
	
	public void testLZO() throws IOException
	{
		compressor = new LZOCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ChannelDataBuffer compressed = compressor.compress(orginal);
			System.out.println(compressed.readableBytes());
			System.out.println(orginal.readableBytes());
			ChannelDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			
			assertEquals(orginal.readableBytes(), restore.readableBytes());
			assertTrue(restore.readableBytes() > (compressed.readableBytes() * 2));
			//assertEquals(orginal, restore);
		}
	}
	
	
	public void testLZF() throws IOException
	{
		compressor = new LZFCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ChannelDataBuffer compressed = compressor.compress(orginal);
			//System.out.println("#####Compress:" + compressed.size());
			ChannelDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			assertEquals(orginal.readableBytes(), restore.readableBytes());
			//System.out.println("#####Original:" + restore.size());
			//System.out.println("#####Compress:" + compressed.size());
			//assertTrue(restore.size() > (compressed.size() * 2));
			//assertEquals(orginal, restore);
			
			
		}
	}

	public void _testGZip() throws IOException
	{
		compressor = new GZipCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ChannelDataBuffer compressed = compressor.compress(orginal);
			//System.out.println("####Compress:" + compressed.size());
			ChannelDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			//System.out.println("#####Compress:" + compressed.size());
			//System.out.println("#####Original:" + restore.size());
			assertEquals(orginal.readableBytes(), restore.readableBytes());
			//assertTrue(restore.size() > (compressed.size() * 2));
			//assertEquals(orginal, restore);
			
		}
		// assertEquals(orginal.size(), restore.size());
		// assertTrue(restore.size() > (compressed.size() * 2));
	}

	public void _testSerai() throws IOException
	{
		compressor = new GZipCompressor();
		HykSerializer serializer = new HykSerializer();

		MessageFragment frag = new MessageFragment();
		frag.setSessionID(1);
		frag.setAddress(new SimpleSockAddress("127.0.0.1", 48101));
		frag.setSequence(1);
		byte[] content = "asdasfsa12312".getBytes();
		frag.setContent(ChannelDataBuffer.wrap(content));
		ChannelDataBuffer data = serializer.serialize(frag);
		ChannelDataBuffer compressed = compressor.compress(data);
		ChannelDataBuffer restore = compressor.decompress(compressed);
		assertEquals(data, restore);
		// assertEquals(orginal.size(), restore.size());
		// assertTrue(restore.size() > (compressed.size() * 2));
	}

}
