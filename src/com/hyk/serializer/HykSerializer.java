/**
 * 
 */
package com.hyk.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyk.serializer.impl.AbstractSerailizerImpl;
import com.hyk.serializer.impl.SerializerImplFactory;
import com.hyk.serializer.io.HykObjectInput;
import com.hyk.serializer.io.BufferedInputStream;
import com.hyk.serializer.io.HykObjectOutput;
import com.hyk.serializer.reflect.DefaultConstructor;
import com.hyk.serializer.util.ClassUtil;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 * 
 */
public class HykSerializer implements Serializer {

	// private Map<Class, DefaultConstructor> deafultConstructorMap = new
	// ConcurrentHashMap<Class, DefaultConstructor>();

	public static long encodeZigZag(final long n) {
		// Note: the right-shift must be arithmetic
		return (n << 1) ^ (n >> 63);
	}

	public static int encodeZigZag(final int n) {
		// Note: the right-shift must be arithmetic
		return (n << 1) ^ (n >> 31);
	}

	public static int encodeZigZag(final short n) {
		// Note: the right-shift must be arithmetic
		return (n << 1) ^ (n >> 15);
	}

	public static int encodeZigZag(final char n) {
		// Note: the right-shift must be arithmetic
		return (n << 1) ^ (n >> 15);
	}

	public static class SizeComputer {
		public static final int LITTLE_ENDIAN_64_SIZE = 8;
		public static final int LITTLE_ENDIAN_32_SIZE = 4;

		public static int computeVarintSize(final short value) {
			if ((value & (0xffffffff << 7)) == 0)
				return 1;
			if ((value & (0xffffffff << 14)) == 0)
				return 2;
			return 3;
		}

		public static int computeVarintSize(final char value) {
			if ((value & (0xffffffff << 7)) == 0)
				return 1;
			if ((value & (0xffffffff << 14)) == 0)
				return 2;
			return 3;
		}

		public static int computeVarintSize(final int value) {
			if ((value & (0xffffffff << 7)) == 0)
				return 1;
			if ((value & (0xffffffff << 14)) == 0)
				return 2;
			if ((value & (0xffffffff << 21)) == 0)
				return 3;
			if ((value & (0xffffffff << 28)) == 0)
				return 4;
			return 5;
		}

		public static int computeVarintSize(final long value) {
			if ((value & (0xffffffffffffffffL << 7)) == 0)
				return 1;
			if ((value & (0xffffffffffffffffL << 14)) == 0)
				return 2;
			if ((value & (0xffffffffffffffffL << 21)) == 0)
				return 3;
			if ((value & (0xffffffffffffffffL << 28)) == 0)
				return 4;
			if ((value & (0xffffffffffffffffL << 35)) == 0)
				return 5;
			if ((value & (0xffffffffffffffffL << 42)) == 0)
				return 6;
			if ((value & (0xffffffffffffffffL << 49)) == 0)
				return 7;
			if ((value & (0xffffffffffffffffL << 56)) == 0)
				return 8;
			if ((value & (0xffffffffffffffffL << 63)) == 0)
				return 9;
			return 10;
		}

		public static int computeSize(boolean value) {
			return 1;
		}

		public static int computeSize(double value) {
			return LITTLE_ENDIAN_64_SIZE;
		}

		public static int computeSize(float value) {
			return LITTLE_ENDIAN_32_SIZE;
		}

		public static int computeSize(byte value) {
			return 1;
		}

		public static int computeSize(byte[] value) {
			return value == null ? 0 : value.length;
		}

		public static int computeSize(char value) {
			return computeVarintSize(encodeZigZag(value));
		}

		public static int computeSize(short value) {
			return computeVarintSize(encodeZigZag(value));
		}

		public static int computeSize(int value) {
			return computeVarintSize(encodeZigZag(value));
		}

		public static int computeSize(long value) {
			return computeVarintSize(encodeZigZag(value));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hyk.serializer.Serializer#deserialize(java.lang.Class, byte[])
	 */
	// @Override
	public <T> T deserialize_(Class<T> type, byte[] data)
			throws NotSerializableException, IOException,
			InstantiationException {
		if (type.isInterface()) {
			throw new InstantiationException(type.getName());
		}
		BufferedInputStream is = new BufferedInputStream(data);
		HykObjectInput in = new HykObjectInput(is, this);
		// return null;
		return in.readObject(type);
	}

	public <T> T deserialize(Class<T> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException {
		if (type.isInterface()) {
			throw new InstantiationException(type.getName());
		}
//		BufferedInputStream is = new BufferedInputStream(data.input);
//		HykObjectInput in = new HykObjectInput(is, this);		
//		T ret = in.readObject(type);
		T ret = (T)SerializerImplFactory.getSerializer(type).unmarshal(type, data);
		data.rewind();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hyk.serializer.Serializer#serialize(java.lang.Object)
	 */
	// @Override
	public byte[] serialize_(Object obj) throws NotSerializableException,
			IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
		HykObjectOutput out = new HykObjectOutput(bos);
		out.writeObject(obj);
		out.flush();
		return bos.toByteArray();
	}

	public ByteArray serialize(Object obj) throws NotSerializableException,
			IOException {
		return serialize(obj, ByteArray.allocate(256));
	}

	public ByteArray serialize(Object obj, ByteArray input)
			throws NotSerializableException, IOException {
		AbstractSerailizerImpl serializer = SerializerImplFactory.getSerializer(obj.getClass());
		serializer.marshal(obj, input);
		return input.flip();
	}

}
