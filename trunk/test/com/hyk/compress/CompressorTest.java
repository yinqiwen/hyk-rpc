package com.hyk.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.hyk.compress.gz.GZipCompressor;
import com.hyk.compress.zip.ZipCompressor;
import com.hyk.rpc.core.address.SimpleSockAddress;
import com.hyk.rpc.core.message.MessageFragment;
import com.hyk.serializer.HykSerializer;
import com.hyk.util.buffer.ByteArray;

import junit.framework.TestCase;

public class CompressorTest extends TestCase {

	ByteArray orginal;
	byte[] orginal1;
	Compressor compressor;
	protected void setUp() throws Exception {
		super.setUp();
		File f = new File("build.xml");
		long size = f.length();
		byte[] orginalb = new byte[(int) size];
		FileInputStream fis = new FileInputStream(f);
		fis.read(orginalb);
		orginal1 = orginalb;
		fis.close();
		orginal = ByteArray.wrap(orginalb);
	}

//	public void testBZ2() throws IOException {
//		compressor = new BZ2Compressor();
//		ByteArray compressed = compressor.compress(orginal);
//		ByteArray restore = compressor.decompress(compressed);
//		assertEquals(orginal, restore);
//		assertTrue(restore.size() > (compressed.size() * 2));
//	}
	
	public void testZip() throws IOException {
		compressor = new ZipCompressor();
		ByteArray compressed = compressor.compress(orginal);
		ByteArray restore = compressor.decompress(compressed);
		
		//assertEquals(orginal, restore);
		assertEquals(orginal.size(), restore.size());
		assertTrue(restore.size() > (compressed.size() * 2));
	}
	
	
	public void testGZip() throws IOException {
		compressor = new GZipCompressor();
		ByteArray compressed = compressor.compress(orginal);
		ByteArray restore = compressor.decompress(compressed);
		assertEquals(orginal, restore);
		//assertEquals(orginal.size(), restore.size());
		//assertTrue(restore.size() > (compressed.size() * 2));
	}
	
	public void testSerai() throws IOException {
		compressor = new GZipCompressor();
		HykSerializer  serializer = new HykSerializer();
		
		MessageFragment frag = new MessageFragment();
		frag.setSessionID(1);
		frag.setAddress(new SimpleSockAddress("127.0.0.1", 48101));
		frag.setSequence(1);
		ByteArray content = ByteArray.wrap("asdasfsa12312".getBytes());
		frag.setContent(content );
		ByteArray data = serializer.serialize(frag);
		ByteArray compressed = compressor.compress(data);
		ByteArray restore = compressor.decompress(compressed);
		assertEquals(data, restore);
		//assertEquals(orginal.size(), restore.size());
		//assertTrue(restore.size() > (compressed.size() * 2));
	}

}