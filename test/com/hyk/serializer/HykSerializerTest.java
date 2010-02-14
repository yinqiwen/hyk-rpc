package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import com.hyk.rpc.core.message.Message;
import com.hyk.rpc.core.message.MessageFactory;
import com.hyk.rpc.core.message.Response;
import com.hyk.util.buffer.ByteArray;

import target.TargetClassTop;
import junit.framework.TestCase;

public class HykSerializerTest extends TestCase {

	static enum TEST{A, B, C};
	
	static class TestF implements Externalizable
	{

		@Override
		public void readExternal(SerializerInput in) throws IOException {
			// TODO Auto-generated method stub
			int i = in.readInt();
			//System.out.println(i);
			i = in.readInt();
			//System.out.println(i);
			long v1 = in.readLong();
			int v2 = in.readInt();
			//System.out.println(v1);
			//System.out.println(v2);
		}

		@Override
		public void writeExternal(SerializerOutput out) throws IOException {
			out.writeInt(1);
			out.writeInt(2);
			out.writeLong(-1);
			out.writeInt(1);
		}
		
	}
	
	static interface TI{ public String say();}
	static String expectedProxyTestResult = "testProxy";
	static class TH implements InvocationHandler,Serializable{

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			return expectedProxyTestResult;
		}}
	
	HykSerializer serializer = null;
	
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
	
//	public void testMessage() throws NotSerializableException, IOException, InstantiationException
//	{
//		TypeValue[] paras = new TypeValue[1];
//		paras[0] = new TypeValue();
//		paras[0].setTypeName("java.lang.String");
//		paras[0].setValue("safasgas");
//		Message msg = MessageFactory.instance.createRequest(-1, 1, paras);
//		ByteArray data = serializer.serialize(msg);
//		serializer.deserialize(Message.class, data);
//		
//	}
	public void testObject2() throws NotSerializableException, IOException, InstantiationException
	{
		TestF t = new TestF();
		ByteArray data = serializer.serialize(t);
		serializer.deserialize(TestF.class, data);
	}
	public void testMessage() throws NotSerializableException, IOException, InstantiationException
	{
		//TypeValue[] paras = new TypeValue[1];
		//paras[0] = new TypeValue();
		//paras[0].setTypeName("java.lang.String");
		//paras[0].setValue("safasgas");
		Object[] paras = new Object[]{"safasgas"};
		Message request = MessageFactory.instance.createRequest(-1, "hello", paras);
		ByteArray data = serializer.serialize(request);
		//byte[] data = serializer.serialize_(msg);
		//System.out.println(new String(data.toByteArray()));
		serializer.deserialize(Message.class, data);
		
		Message response = MessageFactory.instance.createResponse(request, new NullPointerException("waht!"));
		assertNotNull(response.getValue());
		data = serializer.serialize(response);
		System.out.println(data.size());
		response = serializer.deserialize(Message.class, data);
		data = serializer.serialize(request);
		assertNotNull(response.getValue());
		Response res = (Response) response.getValue();
		assertNotNull(res.getReply());
	}
	
	public void testArray() throws NotSerializableException, IOException, InstantiationException
	{
		int[] array = new int[]{0,1,2,456,-789,-48100,10625, 0};
		//byte[] data = serializer.serialize(array);
		ByteArray data = serializer.serialize(array);
		int[] array2 = serializer.deserialize(int[].class, data);
		assertEquals(true, Arrays.equals(array, array2));	
		
		String[] value = new String[]{"hello"};
		data = serializer.serialize(value);
		String[] ret = serializer.deserialize(String[].class, data);
		assertEquals(true, Arrays.equals(value, ret));	
		
		Object[] oa = new Object[]{1,"asdas",3.5, 56567};
		data = serializer.serialize(oa);
		Object[] oa2 = serializer.deserialize(Object[].class, data);
		assertEquals(true, Arrays.equals(oa, oa2));	
		
		byte[] content = new byte[32];
		data = serializer.serialize(content);
		System.out.println("####" + data.size());
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
	
	public void testException() throws NotSerializableException, IOException, InstantiationException
	{
		NullPointerException exception = new NullPointerException("asdasfas");
		ByteArray data = serializer.serialize(exception);
		NullPointerException result = serializer.deserialize(NullPointerException.class, data);
		assertEquals(result.getMessage(), exception.getMessage());
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
		byte[] data = serializer.serialize_(test);
		//ByteArray data = serializer.serialize(test);
		TargetClassTop t = serializer.deserialize_(TargetClassTop.class, data);
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
