package com.hyk.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StandardSerializer implements Serializer {

	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(Class<T> type, byte[] data)
			throws NotSerializableException, IOException,InstantiationException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (T) ois.readObject();
		}  catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	@Override
	public byte[] serialize(Object obj) throws NotSerializableException,IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		return bos.toByteArray();
	}

}
