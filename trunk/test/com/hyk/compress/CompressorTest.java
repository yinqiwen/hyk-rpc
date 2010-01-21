package com.hyk.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.hyk.compress.gz.GZipCompressor;
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

	public void testSevenZip() throws IOException {
		compressor = new SevenZipCompressor();
		ByteArray compressed = compressor.compress(orginal);
		ByteArray restore = compressor.decompress(compressed);
		byte[] raw1 = orginal.rawbuffer();
		byte[] raw2 = restore.rawbuffer();
		
//		for (int i = 0; i < restore.size(); i++) {
//			if(raw1[i] != raw2[i])
//			{
//				System.out.println("###" + i);
//				System.out.println("@@@" + raw1[i]);
//				System.out.println("???" + raw2[i]);
//				//break;
//			}
//		}
		assertEquals(orginal, restore);
		assertTrue(restore.size() > (compressed.size() * 2));
	}
	
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

}
