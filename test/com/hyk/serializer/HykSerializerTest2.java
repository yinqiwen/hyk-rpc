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
import com.hyk.util.buffer.ByteArray;
import com.sun.xml.internal.fastinfoset.stax.events.ReadIterator;

import target.TargetClassTop;
import junit.framework.TestCase;

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
