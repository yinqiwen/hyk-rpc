package com.hyk.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.hyk.util.buffer.ByteArray;

public class StandardSerializer implements Serializer {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(Class<T> type, byte[] data)
			throws NotSerializableException, IOException,
			InstantiationException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (T) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	@Override
	public byte[] serialize(Object obj) throws NotSerializableException,
			IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		return bos.toByteArray();
	}

	public ByteArray serialize_(Object obj) throws NotSerializableException,
			IOException {
		ByteArray array = pool.apply(1024);
		ObjectOutputStream oos = new ObjectOutputStream(array.output);
		oos.writeObject(obj);
		oos.close();
		return array;
	}

	public <T> T deserialize_(Class<T> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		//ByteArrayInputStream bis = new ByteArrayInputStream(data);
		try {
			ObjectInputStream ois = new ObjectInputStream(data.input);
			T ret =  (T) ois.readObject();
			ois.close();
			return ret;
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

}
