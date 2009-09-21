/**
 * 
 */
package com.hyk.rpc.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author qiying.wang
 *
 */
public class JavaSerializer implements ObjectSerializer {
	ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
	/* (non-Javadoc)
	 * @see com.hyk.rpc.serializer.ObjectSerializer#mashall(java.lang.Object)
	 */
	@Override
	public byte[] mashall(Object obj) throws IOException {
		bos.reset();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);		
		return bos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see com.hyk.rpc.serializer.ObjectSerializer#unmashall(byte[])
	 */
	@Override
	public Object unmashall(byte[] value) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(value);
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
	}

}
