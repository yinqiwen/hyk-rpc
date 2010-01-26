/**
 * 
 */
package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.serializer.Serializer;
import com.hyk.serializer.SerializerInput;
import com.hyk.serializer.SerializerOutput;
import com.hyk.serializer.io.Type;
import com.hyk.serializer.reflect.ReflectionCache;
import com.hyk.serializer.util.ClassUtil;
import com.hyk.util.buffer.ByteArray;

/**
 * @author qiying.wang
 * @param <T>
 * 
 */
public abstract class AbstractSerailizerImpl<T> {

	private static final int INDICATOR_DEFAULT = 0;
	private static final int INDICATOR_TYPE = 1;
	private static final int INDICATOR_PROXY = 2;
	private static final int INDICATOR_OTHER = 3;

	protected static class Input implements SerializerInput {
		private ByteArray data;

		public Input(ByteArray data) {
			this.data = data;
		}

		@Override
		public boolean readBoolean() throws IOException {
			return readBool(data);
		}

		@Override
		public byte readByte() throws IOException {
			return AbstractSerailizerImpl.readByte(data);
		}

		@Override
		public char readChar() throws IOException {
			return AbstractSerailizerImpl.readChar(data);
		}

		@Override
		public double readDouble() throws IOException {
			return AbstractSerailizerImpl.readDouble(data);
		}

		@Override
		public float readFloat() throws IOException {
			return AbstractSerailizerImpl.readFloat(data);
		}

		@Override
		public int readInt() throws IOException {
			return AbstractSerailizerImpl.readInt(data);
		}

		@Override
		public long readLong() throws IOException {
			return AbstractSerailizerImpl.readLong(data);
		}

		@Override
		public short readShort() throws IOException {
			return AbstractSerailizerImpl.readShort(data);
		}

		@Override
		public String readString() throws IOException {
			return AbstractSerailizerImpl.readString(data);
		}

		public byte[] readBytes() throws IOException {
			return AbstractSerailizerImpl.readBytes(data);
		}

		@Override
		public <T> T readObject(Class<T> type) throws IOException {
			return AbstractSerailizerImpl.readObject(data, type);
		}
	}

	protected static class Output implements SerializerOutput {
		private ByteArray data;

		public Output(ByteArray data) {
			this.data = data;
		}

		@Override
		public void writeBoolean(boolean value) throws IOException {
			AbstractSerailizerImpl.writeBoolean(data, value);
		}

		@Override
		public void writeByte(byte value) throws IOException {
			AbstractSerailizerImpl.writeByte(data, value);
		}

		@Override
		public void writeChar(char value) throws IOException {
			AbstractSerailizerImpl.writeChar(data, value);
		}

		@Override
		public void writeDouble(double value) throws IOException {
			AbstractSerailizerImpl.writeDouble(data, value);
		}

		@Override
		public void writeFloat(float value) throws IOException {
			AbstractSerailizerImpl.writeFloat(data, value);
		}

		@Override
		public void writeInt(int value) throws IOException {
			AbstractSerailizerImpl.writeInt(data, value);
		}

		@Override
		public void writeLong(long value) throws IOException {
			AbstractSerailizerImpl.writeLong(data, value);
		}

		@Override
		public void writeShort(short value) throws IOException {
			AbstractSerailizerImpl.writeShort(data, value);
		}

		@Override
		public void writeString(String value) throws IOException {
			AbstractSerailizerImpl.writeString(data, value);
		}

		@Override
		public void writeObject(Object value) throws IOException {
			AbstractSerailizerImpl.writeObject(data, value);
		}
		
		@Override
		public void writeObject(Object value, Class declType) throws IOException {
			AbstractSerailizerImpl.writeObject(data, value, declType);
		}

		public void writeBytes(byte[] value) throws IOException {
			if (null != value) {
				writeBytes(value, 0, value.length);
			}
		}

		public void writeBytes(byte[] value, int off, int len)
				throws IOException {
			if (null != value) {
				AbstractSerailizerImpl.writeInt(data, len);
				AbstractSerailizerImpl.writeBytes(data, value, off, len);
			}
		}
	}

	protected static int readRawLittleEndian32(ByteArray data)
			throws IOException {
		final byte b1 = readByte(data);
		final byte b2 = readByte(data);
		final byte b3 = readByte(data);
		final byte b4 = readByte(data);
		return (((int) b1 & 0xff)) | (((int) b2 & 0xff) << 8)
				| (((int) b3 & 0xff) << 16) | (((int) b4 & 0xff) << 24);
	}

	protected static long readRawLittleEndian64(ByteArray data)
			throws IOException {
		final byte b1 = readByte(data);
		final byte b2 = readByte(data);
		final byte b3 = readByte(data);
		final byte b4 = readByte(data);
		final byte b5 = readByte(data);
		final byte b6 = readByte(data);
		final byte b7 = readByte(data);
		final byte b8 = readByte(data);
		return (((long) b1 & 0xff)) | (((long) b2 & 0xff) << 8)
				| (((long) b3 & 0xff) << 16) | (((long) b4 & 0xff) << 24)
				| (((long) b5 & 0xff) << 32) | (((long) b6 & 0xff) << 40)
				| (((long) b7 & 0xff) << 48) | (((long) b8 & 0xff) << 56);
	}

	protected static byte readByte(ByteArray data) throws IOException {
		return (byte) data.input.read();
	}

	protected static long readLong(ByteArray data) throws IOException {
		int shift = 0;
		long result = 0;
		while (shift < 64) {
			final byte b = readByte(data);
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
			shift += 7;
		}
		throw new IOException("encountered a malformed varint");
	}

	protected static short readShort(ByteArray data) throws IOException {
		int shift = 0;
		short result = 0;
		while (shift < 16) {
			final byte b = readByte(data);
			result |= (short) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
			shift += 7;
		}
		throw new IOException("encountered a malformed varint");
	}

	protected static char readChar(ByteArray data) throws IOException {
		int shift = 0;
		char result = 0;
		while (shift < 16) {
			final byte b = readByte(data);
			result |= (short) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
			shift += 7;
		}
		throw new IOException("encountered a malformed varint");
	}

	// protected static long readLong(ByteArray data) throws IOException {
	// int shift = 0;
	// long result = 0;
	// while (shift < 64) {
	// final byte b = readByte(data);
	// result |= (long) (b & 0x7F) << shift;
	// if ((b & 0x80) == 0) {
	// return result;
	// }
	// shift += 7;
	// }
	// throw new IOException("encountered a malformed varint");
	// }

	protected static int readInt(ByteArray data) throws IOException {
		byte tmp = readByte(data);
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7f;
		if ((tmp = readByte(data)) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7f) << 7;
			if ((tmp = readByte(data)) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7f) << 14;
				if ((tmp = readByte(data)) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7f) << 21;
					result |= (tmp = readByte(data)) << 28;
					if (tmp < 0) {
						// Discard upper 32 bits.
						for (int i = 0; i < 5; i++) {
							if (readByte(data) >= 0) {
								return result;
							}
						}
						throw new IOException("encountered a malformed varint");
					}
				}
			}
		}
		return result;
	}

	protected static boolean readBool(ByteArray data) throws IOException {
		return readInt(data) != 0;
	}

	protected static byte[] readBytes(ByteArray data) throws IOException {
		int size = readInt(data);
		if (size > 0 && data.input.available() >= size) {
			byte[] ret = new byte[size];
			data.input.read(ret);
			return ret;
		} else {
			throw new IOException("No enoght data in stream!");
		}

	}

	protected static double readDouble(ByteArray data) throws IOException {
		return Double.longBitsToDouble(readRawLittleEndian64(data));
	}

	protected static float readFloat(ByteArray data) throws IOException {
		return Float.intBitsToFloat(readRawLittleEndian32(data));
	}

	protected static int readTag(ByteArray data) throws IOException {
		if (data.input.available() == 0) {
			return 0;
		}
		int tag = readInt(data);
		return tag >>> 3;
	}

	// protected static void read(ByteArray data, byte[] bytes) throws
	// IOException {
	// //return Float.intBitsToFloat(readRawLittleEndian32(data));
	// }

	protected static String readString(ByteArray data) throws IOException {
		int size = readInt(data);
		if (data.input.available() >= size) {

			String s = new String(data.rawbuffer(), data.position(), size,
					"UTF-8");
			data.position(data.position() + size);
			return s;
		} else {
			throw new IOException("No enoght data in stream!");
		}
	}

	protected static void writeByte(ByteArray data, final byte value)
			throws IOException {
		data.output.write(value);
	}

	protected static void writeBytes(ByteArray data, final byte[] value,
			int off, int len) throws IOException {
		data.output.write(value, off, len);
	}

	protected static void writeBytes(ByteArray data, final byte[] value)
			throws IOException {
		data.output.write(value);
	}

	protected static void writeByte(ByteArray data, final int value)
			throws IOException {
		writeByte(data, (byte) value);
	}

	protected static void writeBoolean(ByteArray data, boolean value)
			throws IOException {
		writeByte(data, value ? 1 : 0);
	}

	protected static void write(ByteArray data, int value) throws IOException {
		writeInt(data, value);
	}

	protected static void writeInt(ByteArray data, int value)
			throws IOException {
		if (value >= 0) {
			while (true) {
				if ((value & ~0x7F) == 0) {
					writeByte(data, (byte) value);
					return;
				} else {
					writeByte(data, (value & 0x7F) | 0x80);
					value >>>= 7;
				}
			}
		} else {
			writeLong(data, value);
		}

	}

	protected static void writeShort(ByteArray data, int value)
			throws IOException {
		writeShort(data, (short) value);
	}

	protected static void writeShort(ByteArray data, short value)
			throws IOException {
		if (value >= 0) {
			while (true) {
				if ((value & ~0x7FL) == 0) {
					writeByte(data, (int) value);
					return;
				} else {
					writeByte(data, ((int) value & 0x7F) | 0x80);
					value >>>= 7;
				}
			}
		} else {
			writeInt(data, value);
		}
	}

	protected static void writeChar(ByteArray data, int value)
			throws IOException {
		writeChar(data, (char) value);
	}

	protected static void writeChar(ByteArray data, char value)
			throws IOException {
		writeShort(data, value);
	}

	protected static void writeLong(ByteArray data, long value)
			throws IOException {
		while (true) {
			if ((value & ~0x7FL) == 0) {
				writeByte(data, (int) value);
				return;
			} else {
				writeByte(data, ((int) value & 0x7F) | 0x80);
				value >>>= 7;
			}
		}
	}

	protected static void writeRawLittleEndian32(ByteArray data, final int value)
			throws IOException {
		writeByte(data, (value) & 0xFF);
		writeByte(data, (value >> 8) & 0xFF);
		writeByte(data, (value >> 16) & 0xFF);
		writeByte(data, (value >> 24) & 0xFF);
	}

	protected static void writeRawLittleEndian64(ByteArray data,
			final long value) throws IOException {
		writeByte(data, (int) (value) & 0xFF);
		writeByte(data, (int) (value >> 8) & 0xFF);
		writeByte(data, (int) (value >> 16) & 0xFF);
		writeByte(data, (int) (value >> 24) & 0xFF);
		writeByte(data, (int) (value >> 32) & 0xFF);
		writeByte(data, (int) (value >> 40) & 0xFF);
		writeByte(data, (int) (value >> 48) & 0xFF);
		writeByte(data, (int) (value >> 56) & 0xFF);
	}

	protected static void writeFloat(ByteArray data, final float value)
			throws IOException {
		writeRawLittleEndian32(data, Float.floatToRawIntBits(value));
	}

	protected static void writeDouble(ByteArray data, final double value)
			throws IOException {
		writeRawLittleEndian64(data, Double.doubleToRawLongBits(value));
	}

	protected static void writeTag(ByteArray data, int tag) throws IOException {
		tag = ((tag << 3) | 0);
		write(data, tag);
	}

	protected static void write(ByteArray data, byte[] b) throws IOException {
		// buffer.put(b);
		data.output.write(b);
	}

	protected static void writeString(ByteArray data, String s)
			throws IOException {
		if (null == s)
			return;
		final byte[] bytes = s.getBytes("UTF-8");
		writeInt(data, bytes.length);
		write(data, bytes);
	}

	protected static <T> T readObject(ByteArray data, Class<T> type)
			throws IOException {
		Type dataType = ReflectionCache.getType(type);
		int indicator = readTag(data);
		try {
			switch (indicator) {
			case INDICATOR_TYPE: {
				
				type = (Class<T>) Class.forName(readString(data));
				
				dataType = ReflectionCache.getType(type);
				break;
			}
			case INDICATOR_PROXY:
			{
				dataType = Type.PROXY;
				break;
			}
			case INDICATOR_OTHER:
			{
				dataType = Type.OTHER;
				break;
			}
			default:
				break;
			}
			if(!data.hasRemaining())
			{
				return null;
			}
			return (T) SerializerImplFactory.getSerializer(dataType).unmarshal(
					type, data);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			
			throw new IOException(e);
		}

	}

	protected static void writeObject(ByteArray data, Object value, Class type)
			throws IOException {
		//Class queryType = type;
		Class clazz = value.getClass();
		Type dataType = ReflectionCache.getType(clazz);
		switch (dataType) {
		case PROXY: {
			writeTag(data,INDICATOR_PROXY);
			break;
		}
		case OTHER:
		{
			writeTag(data,INDICATOR_OTHER);
			break;
		}
		default: {
			if (ClassUtil.equals(clazz, type)) {
				writeTag(data, INDICATOR_DEFAULT);
			} else {
				writeTag(data, INDICATOR_TYPE);
				writeString(data, clazz.getName());
			}
			dataType = ReflectionCache.getType(clazz);
			break;
		}
		}
		SerializerImplFactory.getSerializer(dataType).marshal(value, data);
	}

	protected static void writeObject(ByteArray data, Object value)
			throws IOException {
		if(null != value)
		{
			writeObject(data, value, value.getClass());
		}
		
	}


	public abstract ByteArray marshal(T obj, ByteArray data)
			throws NotSerializableException, IOException;

	public abstract T unmarshal(Class<T> type, ByteArray data)
			throws NotSerializableException, IOException,
			InstantiationException;

}
