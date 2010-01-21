package com.hyk.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.hyk.compress.gz.GZipCompressor;
import com.hyk.compress.gz.GZipCompressor2;
import com.hyk.compress.sevenzip.SevenZipCompressor;
import com.hyk.compress.zip.ZipCompressor;
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

//	public void testSevenZip() throws IOException {
//		compressor = new SevenZipCompressor();
//		ByteArray compressed = compressor.compress(orginal);
//		ByteArray restore = compressor.decompress(compressed);
//		assertEquals(orginal, restore);
//		assertTrue(restore.size() > (compressed.size() * 2));
//	}
	
	public void testZip() throws IOException {
		compressor = new ZipCompressor();
		ByteArray compressed = compressor.compress(orginal);
		ByteArray restore = compressor.decompress(compressed);
		assertEquals(orginal, restore);
		assertEquals(orginal.size(), restore.size());
		assertTrue(restore.size() > (compressed.size() * 2));
	}
	
	public void testGZip1() throws IOException {
		GZipCompressor compressor1 = new GZipCompressor();
		byte[] compressed = compressor1.compress(orginal1, 0, orginal1.length);
		byte[] restore = compressor1.decompress(compressed, 0, compressed.length);
		//assertEquals(orginal, restore);
		//assertEquals(orginal.size(), restore.size());
		assertTrue(Arrays.equals(orginal1, restore));
		assertTrue(restore.length > (compressed.length * 2));
	}
	
	public void testGZip() throws IOException {
		compressor = new GZipCompressor2();
		ByteArray compressed = compressor.compress(orginal);
		GZipCompressor compressor1 = new GZipCompressor();
		byte[] compressed1 = compressor1.compress(orginal1, 0, orginal1.length);
		assertEquals(compressed, ByteArray.wrap(compressed1));
		ByteArray restore = compressor.decompress(compressed);
		assertEquals(orginal, restore);
		//assertEquals(orginal.size(), restore.size());
		//assertTrue(restore.size() > (compressed.size() * 2));
	}

}
