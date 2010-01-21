package com.hyk.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.hyk.serializer.impl.SerializerImplFactory;
import com.hyk.util.buffer.ByteArray;

public class StandardSerializer implements Serializer
{

	// @SuppressWarnings("unchecked")
	// @Override
	// public <T> T deserialize(Class<T> type, byte[] data)
	// throws NotSerializableException, IOException,
	// InstantiationException {
	// ByteArrayInputStream bis = new ByteArrayInputStream(data);
	// try {
	// ObjectInputStream ois = new ObjectInputStream(bis);
	// return (T) ois.readObject();
	// } catch (ClassNotFoundException e) {
	// throw new IOException(e);
	// }
	// }
	//
	// @Override
	// public byte[] serialize(Object obj) throws NotSerializableException,
	// IOException {
	//
	// ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
	// ObjectOutputStream oos = new ObjectOutputStream(bos);
	// oos.writeObject(obj);
	// return bos.toByteArray();
	// }

	public ByteArray serialize(Object obj) throws NotSerializableException, IOException
	{
		return serialize(obj, ByteArray.allocate(32));
	}

	public ByteArray serialize(Object obj, ByteArray input) throws NotSerializableException, IOException
	{
		// ByteArray array = ByteArray.allocate(1024);
		// ObjectOutputStream oos = new ObjectOutputStream(input.output);
		// oos.writeObject(obj);
		SerializerImplFactory.otherSerializer.serialize(obj, input);
		return input.flip();
	}

	public <T> T deserialize(Class<T> type, ByteArray data) throws NotSerializableException, IOException, InstantiationException
	{
		// ByteArrayInputStream bis = new ByteArrayInputStream(data);

		// ObjectInputStream ois = new ObjectInputStream(data.input);
		// T ret = (T) ois.readObject();
		T ret = SerializerImplFactory.otherSerializer.deserialize(type, data);
		data.rewind();
		return ret;

	}

}
