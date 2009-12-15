package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Arrays;

import target.TargetClassTop;
import junit.framework.TestCase;

public class HykSerializerTest extends TestCase {

	static enum TEST{A, B, C};
	
	Serializer serializer = null;
	
	protected void setUp()
	{
		serializer = new HykSerializer();
	}
	
	public void testInt() throws NotSerializableException, IOException, InstantiationException
	{
		int expected = 1;
		byte[] data = serializer.serialize(expected);
		int actually = serializer.deserialize(int.class, data);
		assertEquals(actually, expected);
		
		expected = -1;
		data = serializer.serialize(expected);
		actually = serializer.deserialize(int.class, data);
		assertEquals(actually, expected);
		
		expected = 102456;
		data = serializer.serialize(expected);
		actually = serializer.deserialize(int.class, data);
		assertEquals(actually, expected);
		
		expected = 0;
		data = serializer.serialize(expected);
		actually = serializer.deserialize(int.class, data);
		assertEquals(actually, expected);
		
	}
	
	public void testArray() throws NotSerializableException, IOException, InstantiationException
	{
		int[] array = new int[]{1,2,456,-789,-48100,10625, 0};
		byte[] data = serializer.serialize(array);
		int[] array2 = serializer.deserialize(int[].class, data);
		assertEquals(true, Arrays.equals(array, array2));	
	}
	
	public void testEnum() throws NotSerializableException, IOException, InstantiationException
	{
		byte[] data = serializer.serialize(TEST.A);
		TEST result = serializer.deserialize(TEST.class, data);
		assertEquals(result, TEST.A);
		
		data = serializer.serialize(TEST.C);
		result = serializer.deserialize(TEST.class, data);
		assertEquals(result, TEST.C);
	}
	
	public void testByte() throws NotSerializableException, IOException, InstantiationException
	{
		byte b = (byte) 128;
		byte[] data = serializer.serialize(b);
		byte result = serializer.deserialize(byte.class, data);
		assertEquals(result, b);
		
		Byte b1 = (byte) -127;
		data = serializer.serialize(b1);
		Byte result1 = serializer.deserialize(Byte.class, data);
		assertEquals(result1, b1);
	}
	
	public void testString() throws NotSerializableException, IOException, InstantiationException
	{
		byte[] data = serializer.serialize("hello,world");
		String result = serializer.deserialize(String.class, data);
		assertEquals(result, "hello,world");
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException {
		// TODO Auto-generated method stub
		TargetClassTop test = new TargetClassTop();
		test.setName("wangqiying!");
		Serializer serializer = new HykSerializer();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			//byte[] buf = new byte[100];
			serializer.serialize(test);
		}
		byte[] data = serializer.serialize(test);
		long end = System.currentTimeMillis();
		System.out.println("####Serialize time:" + (end - start));

		System.out.println("####Serialize size:" + data.length);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			//byte[] buf = new byte[100];
			serializer.deserialize(TargetClassTop.class, data);
		}
		test = serializer.deserialize(TargetClassTop.class, data);
		end = System.currentTimeMillis();
		System.out.println("####Deserialize time:" + (end - start));
		System.out.println("####" + test.getName());
	}

	
}
