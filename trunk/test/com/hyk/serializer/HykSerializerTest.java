package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import com.hyk.util.buffer.ByteArray;

import target.TargetClassTop;
import junit.framework.TestCase;

public class HykSerializerTest extends TestCase {

	static enum TEST{A, B, C};
	
	static interface TI{ public String say();}
	static String expectedProxyTestResult = "testProxy";
	static class TH implements InvocationHandler,Serializable{

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			return expectedProxyTestResult;
		}}
	
	Serializer serializer = null;
	
	protected void setUp()
	{
		serializer = new HykSerializer();
	}
	
	public void testInt() throws NotSerializableException, IOException, InstantiationException
	{
		int expected = 1;
		//byte[] data = serializer.serialize(expected);
		ByteArray data = serializer.serialize(expected);
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
		//byte[] data = serializer.serialize(array);
		ByteArray data = serializer.serialize(array);
		int[] array2 = serializer.deserialize(int[].class, data);
		assertEquals(true, Arrays.equals(array, array2));	
		
		String[] value = new String[]{"hello"};
		data = serializer.serialize(value);
		String[] ret = serializer.deserialize(String[].class, data);
		assertEquals(true, Arrays.equals(value, ret));	
	}
	
	public void testEnum() throws NotSerializableException, IOException, InstantiationException
	{
		//byte[] data = serializer.serialize(TEST.A);
		ByteArray data = serializer.serialize(TEST.A);
		TEST result = serializer.deserialize(TEST.class, data);
		assertEquals(result, TEST.A);
		
		data = serializer.serialize(TEST.C);
		result = serializer.deserialize(TEST.class, data);
		assertEquals(result, TEST.C);
	}
	
	public void testByte() throws NotSerializableException, IOException, InstantiationException
	{
		byte b = (byte) 128;
		//byte[] data = serializer.serialize(b);
		ByteArray data = serializer.serialize(b);
		byte result = serializer.deserialize(byte.class, data);
		assertEquals(result, b);
		
		Byte b1 = (byte) -127;
		data = serializer.serialize(b1);
		Byte result1 = serializer.deserialize(Byte.class, data);
		assertEquals(result1, b1);
	}
	
	public void testString() throws NotSerializableException, IOException, InstantiationException
	{
		//byte[] data = serializer.serialize("hello,world");
		ByteArray data = serializer.serialize("hello,world");
		String result = serializer.deserialize(String.class, data);
		assertEquals(result, "hello,world");
	}
	
	public void testProxy() throws NotSerializableException, IOException, IllegalArgumentException, InstantiationException
	{
		Object obj = Proxy.newProxyInstance(HykSerializerTest.class.getClassLoader(), new Class[]{TI.class}, new TH());
		//byte[] data = serializer.serialize(obj);
		ByteArray data = serializer.serialize(obj);
		TI t = (TI) serializer.deserialize(Proxy.getProxyClass(HykSerializerTest.class.getClassLoader(), new Class[]{TI.class}), data);
		assertEquals(expectedProxyTestResult, t.say());
	}
	
	public void testObject() throws NotSerializableException, IOException, IllegalArgumentException, InstantiationException
	{
		TargetClassTop test = new TargetClassTop();
		test.setName("wangqiying!");
		//byte[] data = serializer.serialize(obj);
		ByteArray data = serializer.serialize(test);
		TargetClassTop t = serializer.deserialize(TargetClassTop.class, data);
		assertEquals("wangqiying!", t.getName());
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException {
		// TODO Auto-generated method stub
		TargetClassTop test = new TargetClassTop();
		test.setName("wangqiying!");
		HykSerializer serializer = new HykSerializer();
		//Serializer serializer = new StandardSerializer();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 999999; i++) {
			//byte[] buf = new byte[100];
			//byte[] data = serializer.serialize_(test);
			ByteArray array = serializer.serialize(test);
			//array.free();
		}
		//byte[] data = serializer.serialize_(test);
		ByteArray array = serializer.serialize(test);
		long end = System.currentTimeMillis();
		System.out.println("####Serialize time:" + (end - start));

		System.out.println("####Serialize size:" + array.size());
		//System.out.println("####Serialize size:" + data.length);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 999999; i++) {
			//System.out.println("????");
			//byte[] buf = new byte[100];
			//serializer.deserialize_(TargetClassTop.class, data);
			serializer.deserialize(TargetClassTop.class, array);
		}
		//test = serializer.deserialize_(TargetClassTop.class, data);
		test = serializer.deserialize(TargetClassTop.class, array);
		end = System.currentTimeMillis();
		System.out.println("####Deserialize time:" + (end - start));
		System.out.println("####" + test.getName());
	}

	
}
