package com.hyk.rpc.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

public class ProtocolBufferSeriallizer implements ObjectSerializer {

	ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
	
	CodedOutputStream cos = CodedOutputStream.newInstance(bos);
	@Override
	public byte[] mashall(Object obj) throws IOException {
		bos.reset();
		
		if(null == obj)
		{
			cos.writeStringNoTag("$NIL");
		}
		
		Field[] fs = obj.getClass().getDeclaredFields();
		int tagNo = 1;
		
		
		return null;
	}

	@Override
	public Object unmashall(byte[] value) throws IOException,
			ClassNotFoundException {
		CodedInputStream cis = CodedInputStream.newInstance(value);
		return null;
	}

}
