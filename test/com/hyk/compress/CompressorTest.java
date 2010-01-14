package com.hyk.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.hyk.compress.gz.GZipCompressor;
import com.hyk.compress.sevenzip.SevenZipCompressor;
import com.hyk.compress.zip.ZipCompressor;

import junit.framework.TestCase;

public class CompressorTest extends TestCase {

	byte[] orginal;
	Compressor compressor;
	protected void setUp() throws Exception {
		super.setUp();
		File f = new File("build.xml");
		long size = f.length();
		orginal = new byte[(int) size];
		FileInputStream fis = new FileInputStream(f);
		fis.read(orginal);
		fis.close();
	}

	public void testSevenZip() throws IOException {
		compressor = new SevenZipCompressor();
		byte[] compressed = compressor.compress(orginal);
		byte[] restore = compressor.decompress(compressed);
		assertTrue(Arrays.equals(orginal, restore));
		assertTrue(restore.length > (compressed.length * 2));
	}
	
	public void testZip() throws IOException {
		compressor = new ZipCompressor();
		byte[] compressed = compressor.compress(orginal);
		byte[] restore = compressor.decompress(compressed);
		assertTrue(Arrays.equals(orginal, restore));
		assertTrue(restore.length > (compressed.length * 2));
	}
	
	public void testGZip() throws IOException {
		compressor = new GZipCompressor();
		byte[] compressed = compressor.compress(orginal);
		byte[] restore = compressor.decompress(compressed);
		assertTrue(Arrays.equals(orginal, restore));
		assertTrue(restore.length > (compressed.length * 2));
	}

}
