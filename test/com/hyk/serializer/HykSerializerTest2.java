package com.hyk.serializer;

import java.io.IOException;

import junit.framework.TestCase;

import com.hyk.util.buffer.ByteArray;

public class HykSerializerTest2 extends TestCase {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException {
		HykSerializer serializer = new HykSerializer();
		Object[] oa = new Object[]{100,2};
		ByteArray data = serializer.serialize(oa);
		Object[] oa2 = serializer.deserialize(Object[].class, data);
		System.out.println("#####" + oa2[0]);
		//assertEquals(true, Arrays.equals(oa, oa2));	
	}

	
}
