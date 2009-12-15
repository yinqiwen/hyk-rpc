/**
 * 
 */
package com.hyk.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.OutputStream;

import com.hyk.serializer.util.BufferedInputStream;
import com.hyk.serializer.util.ObjectIOHandler;
import com.hyk.serializer.util.ObjectIOHandlerFactory;
import com.hyk.serializer.util.ReflectObjectIOHandler2;


/**
 * @author Administrator
 * 
 */
public class HykSerializer implements Serializer {

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

	public static class Output {
			
		OutputStream stream;

		public Output(OutputStream stream)
		{
			this.stream = stream;
		}

		public void close() throws IOException {
			stream.close();
		}

		public void flush() throws IOException {
			stream.flush();
		}

		public void write(byte[] b) throws IOException {
			//buffer.put(b);
			stream.write(b);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			//buffer.put(b, off, len);
			stream.write(b,off,len);
		}

		public void writeObject(Object obj) throws IOException {
			if(null == obj) return;
			ObjectIOHandler handler = ObjectIOHandlerFactory.getObjectIOHandler(obj.getClass());
			handler.write(obj, this);
		}

		public void writeBytes(String s) throws IOException {
			if(null == s) return;
			final byte[] bytes = s.getBytes("UTF-8");
			writeInt(bytes.length);
			write(bytes);
		}

		public void writeChars(String s) throws IOException {
			// TODO Auto-generated method stub
			writeBytes(s);
		}

		public void writeByte(final byte value) throws IOException {
			stream.write(value);
		}

		public void writeByte(final int value) throws IOException {
			writeByte((byte) value);
		}

		public void writeBoolean(boolean value) throws IOException {
			writeByte(value ? 1 : 0);
		}

		public void write(int value) throws IOException {
			writeInt(value);
		}

		public void writeInt(int value) throws IOException {
			if(value >= 0)
			{
				while (true) {
					if ((value & ~0x7F) == 0) {
						writeByte((byte) value);
						return;
					} else {
						writeByte((value & 0x7F) | 0x80);
						value >>>= 7;
					}
				}
			}
			else
			{
				writeLong(value);
			}
			
		}

		public void writeShort(int value) throws IOException {
			writeShort((short) value);
		}

		public void writeShort(short value) throws IOException {
			if(value >= 0)
			{
				while (true) {
					if ((value & ~0x7FL) == 0) {
						writeByte((int) value);
						return;
					} else {
						writeByte(((int) value & 0x7F) | 0x80);
						value >>>= 7;
					}
				}
			}
			else
			{
				writeInt(value);
			}
		}

		public void writeChar(int value) throws IOException {
			writeChar((char) value);
		}

		public void writeChar(char value) throws IOException {
			writeShort(value);
		}

		public void writeLong(long value) throws IOException {
			while (true) {
				if ((value & ~0x7FL) == 0) {
					writeByte((int) value);
					return;
				} else {
					writeByte(((int) value & 0x7F) | 0x80);
					value >>>= 7;
				}
			}
		}

		public void writeRawLittleEndian32(final int value) throws IOException {
			writeByte((value) & 0xFF);
			writeByte((value >> 8) & 0xFF);
			writeByte((value >> 16) & 0xFF);
			writeByte((value >> 24) & 0xFF);
		}

		public void writeRawLittleEndian64(final long value) throws IOException {
			writeByte((int) (value) & 0xFF);
			writeByte((int) (value >> 8) & 0xFF);
			writeByte((int) (value >> 16) & 0xFF);
			writeByte((int) (value >> 24) & 0xFF);
			writeByte((int) (value >> 32) & 0xFF);
			writeByte((int) (value >> 40) & 0xFF);
			writeByte((int) (value >> 48) & 0xFF);
			writeByte((int) (value >> 56) & 0xFF);
		}

		public void writeFloat(final float value) throws IOException {
			writeRawLittleEndian32(Float.floatToRawIntBits(value));
		}

		public void writeDouble(final double value) throws IOException {
			writeRawLittleEndian64(Double.doubleToRawLongBits(value));
		}

		public void writeTag(int tag) throws IOException {
			tag = ((tag << 3) | 0);
			write(tag);
		}

		public void writeUTF(String s) throws IOException {
			writeChars(s);
		}

	}

	public static class Input {

		//InputStream is;
		BufferedInputStream is;
		//Class

		public Input(BufferedInputStream is)
		{
			this.is = is;
		}
		
		public int readRawLittleEndian32() throws IOException {
			final byte b1 = readByte();
			final byte b2 = readByte();
			final byte b3 = readByte();
			final byte b4 = readByte();
			return (((int) b1 & 0xff)) | (((int) b2 & 0xff) << 8)
					| (((int) b3 & 0xff) << 16) | (((int) b4 & 0xff) << 24);
		}

		public long readRawLittleEndian64() throws IOException {
			final byte b1 = readByte();
			final byte b2 = readByte();
			final byte b3 = readByte();
			final byte b4 = readByte();
			final byte b5 = readByte();
			final byte b6 = readByte();
			final byte b7 = readByte();
			final byte b8 = readByte();
			return (((long) b1 & 0xff)) | (((long) b2 & 0xff) << 8)
					| (((long) b3 & 0xff) << 16) | (((long) b4 & 0xff) << 24)
					| (((long) b5 & 0xff) << 32) | (((long) b6 & 0xff) << 40)
					| (((long) b7 & 0xff) << 48) | (((long) b8 & 0xff) << 56);
		}

	
		public int available() throws IOException {
			return is.available();
		}

	
		public void close() throws IOException {
			is.close();       
		}

		
		public int read() throws IOException {
			// TODO Auto-generated method stub
			return readByte();
		}

		
		public int read(byte[] b) throws IOException {
			return is.read(b);
		}

		
		public int read(byte[] b, int off, int len) throws IOException {
			return is.read(b, off, len);
		}

		
		public <T> T readObject(Class<T> clazz) throws IOException {
			ObjectIOHandler handler = ObjectIOHandlerFactory.getObjectIOHandler(clazz);
			return handler.read(clazz, this);
		}

		
		public long skip(long n) throws IOException {
			return is.skip(n);
		}

		
		public boolean readBoolean() throws IOException {
			// TODO Auto-generated method stub
			return readInt() != 0;
		}

		
		public void readFully(byte[] b) throws IOException {
			is.read(b);
		}

		
		public void readFully(byte[] b, int off, int len) throws IOException {
			is.read(b, off, len);
		}

		
		public String readLine() throws IOException {
			// TODO Auto-generated method stub
			return readUTF();
		}

		public byte readByte() throws IOException {
			return (byte) is.read();
		}

		public long readLong() throws IOException {
			int shift = 0;
			long result = 0;
			while (shift < 64) {
				final byte b = readByte();
				result |= (long) (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return result;
				}
				shift += 7;
			}
			throw new IOException("encountered a malformed varint");
		}

		public short readShort() throws IOException {
			int shift = 0;
			short result = 0;
			while (shift < 16) {
				final byte b = readByte();
				result |= (short) (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return result;
				}
				shift += 7;
			}
			throw new IOException("encountered a malformed varint");
		}

		public char readChar() throws IOException {
			int shift = 0;
			char result = 0;
			while (shift < 16) {
				final byte b = readByte();
				result |= (short) (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return result;
				}
				shift += 7;
			}
			throw new IOException("encountered a malformed varint");
		}

		public int readInt() throws IOException {
			byte tmp = readByte();
			if (tmp >= 0) {
				return tmp;
			}
			int result = tmp & 0x7f;
			if ((tmp = readByte()) >= 0) {
				result |= tmp << 7;
			} else {
				result |= (tmp & 0x7f) << 7;
				if ((tmp = readByte()) >= 0) {
					result |= tmp << 14;
				} else {
					result |= (tmp & 0x7f) << 14;
					if ((tmp = readByte()) >= 0) {
						result |= tmp << 21;
					} else {
						result |= (tmp & 0x7f) << 21;
						result |= (tmp = readByte()) << 28;
						if (tmp < 0) {
							// Discard upper 32 bits.
							for (int i = 0; i < 5; i++) {
								if (readByte() >= 0) {
									return result;
								}
							}
							throw new IOException(
									"encountered a malformed varint");
						}
					}
				}
			}
			return result;
		}

		public boolean readBool() throws IOException {
			return readInt() != 0;
		}

		public double readDouble() throws IOException {
			return Double.longBitsToDouble(readRawLittleEndian64());
		}

		public float readFloat() throws IOException {
			return Float.intBitsToFloat(readRawLittleEndian32());
		}

		
		public String readUTF() throws IOException {
			int size = readInt();
			return is.readString(size);

		}

		
		public int readUnsignedByte() throws IOException {
			// TODO Auto-generated method stub
			return read();
		}

	
		public int readUnsignedShort() throws IOException {
			
			return readInt();
		}

		public int skipBytes(int n) throws IOException {
			return (int) is.skip(n);
		}
		
		public int readTag()throws IOException 
		{
			if(is.available() == 0)
			{
				return 0;
			}
			int tag = readInt();
			return tag >>> 3;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hyk.serializer.Serializer#deserialize(java.lang.Class, byte[])
	 */
	@Override
	public <T> T deserialize(Class<T> type, byte[] data)
			throws NotSerializableException, IOException,InstantiationException  {
		if(type.isInterface())
		{
			throw new InstantiationException(type.getName());
		}
		BufferedInputStream is = new BufferedInputStream(data);
		Input in = new Input(is);
		//return null;
		return in.readObject(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hyk.serializer.Serializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Object obj) throws NotSerializableException, IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(32);
		Output out = new Output(bos);
		out.writeObject(obj);
		out.flush();
		return bos.toByteArray();
	}

}
