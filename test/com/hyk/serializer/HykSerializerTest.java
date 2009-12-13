package com.hyk.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import target.TargetClassTop;
import junit.framework.TestCase;

public class HykSerializerTest extends TestCase {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
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
