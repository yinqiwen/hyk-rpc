package com.hyk.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.hyk.compress.compressor.Compressor;
import com.hyk.compress.compressor.gz.GZipCompressor;
import com.hyk.compress.compressor.zip.ZipCompressor;
import com.hyk.io.ByteDataBuffer;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.serializer.HykSerializer;
import com.hyk.util.buffer.ByteArray;

import compress.lzf.LZFCompressor;
import compress.quicklz.QuickLZCompressor;

import junit.framework.TestCase;

public class CompressorTest extends TestCase
{

	ByteDataBuffer	orginal;
	byte[]		orginal1;
	Compressor	compressor;
	int			repeat	= 1000;

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
		orginal = ByteDataBuffer.wrap(orginalb);
	}

	// public void testBZ2() throws IOException {
	// compressor = new BZ2Compressor();
	// ByteArray compressed = compressor.compress(orginal);
	// ByteArray restore = compressor.decompress(compressed);
	// assertEquals(orginal, restore);
	// assertTrue(restore.size() > (compressed.size() * 2));
	// }

	public void testZip() throws IOException
	{
		compressor = new ZipCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ByteDataBuffer compressed = compressor.compress(orginal);
			
			//System.out.println("#####Compress:" + compressed.size());
			ByteDataBuffer restore = compressor.decompress(compressed);
			
			//assertEquals(orginal, restore);
			//System.out.println("#####Original:" + restore.size());
			assertEquals(orginal.size(), restore.size());
			
			
			assertTrue(restore.size() > (compressed.size() * 2));
		}

	}

//	public void _testQuickLZ() throws IOException
//	{
//		compressor = new QuickLZCompressor();
//		for(int i = 0; i < repeat; i++)
//		{
//			ByteDataBuffer compressed = compressor.compress(orginal);
//			ByteDataBuffer restore = compressor.decompress(compressed);
//			//assertEquals(orginal, restore);
//			assertEquals(orginal.size(), restore.size());
//			assertTrue(restore.size() > (compressed.size() * 2));
//			//assertEquals(orginal, restore);
//		}
//	}
	
	public void testLZF() throws IOException
	{
		compressor = new LZFCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ByteDataBuffer compressed = compressor.compress(orginal);
			//System.out.println("#####Compress:" + compressed.size());
			ByteDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			assertEquals(orginal.size(), restore.size());
			//System.out.println("#####Original:" + restore.size());
			//System.out.println("#####Compress:" + compressed.size());
			//assertTrue(restore.size() > (compressed.size() * 2));
			//assertEquals(orginal, restore);
			
			
		}
	}

	public void testGZip() throws IOException
	{
		compressor = new GZipCompressor();
		for(int i = 0; i < repeat; i++)
		{
			ByteDataBuffer compressed = compressor.compress(orginal);
			//System.out.println("####Compress:" + compressed.size());
			ByteDataBuffer restore = compressor.decompress(compressed);
			//assertEquals(orginal, restore);
			//System.out.println("#####Compress:" + compressed.size());
			//System.out.println("#####Original:" + restore.size());
			assertEquals(orginal.size(), restore.size());
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
		ByteBuffer content = ByteBuffer.wrap("asdasfsa12312".getBytes());
		frag.setContent(content);
		ByteDataBuffer data = serializer.serialize(frag);
		ByteDataBuffer compressed = compressor.compress(data);
		ByteDataBuffer restore = compressor.decompress(compressed);
		assertEquals(data, restore);
		// assertEquals(orginal.size(), restore.size());
		// assertTrue(restore.size() > (compressed.size() * 2));
	}

}
