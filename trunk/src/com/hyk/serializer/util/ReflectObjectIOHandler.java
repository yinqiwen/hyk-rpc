package com.hyk.serializer.util;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.hyk.serializer.Externalizable;
import com.hyk.serializer.HykSerializer.Input;
import com.hyk.serializer.HykSerializer.Output;

public class ReflectObjectIOHandler implements ObjectIOHandler {

	static enum Type {
		BOOL(0), BYTE(1), INT(2), LONG(3), FLOAT(4), DOUBLE(5), SHORT(6), CHAR(7), STRING(8), ARRAY(9), OBJECT(10);
		
		private int value;
		private Type(int value){this.value = value;}
		public final int getValue() { return value; }
	}

	static Map<Class, Type> reservedClassTable = new HashMap<Class, Type>();
	static {
		reservedClassTable.put(byte.class, Type.BYTE);
		reservedClassTable.put(Byte.class, Type.BYTE);
		reservedClassTable.put(char.class, Type.CHAR);
		reservedClassTable.put(Character.class, Type.CHAR);
		reservedClassTable.put(float.class, Type.FLOAT);
		reservedClassTable.put(Float.class, Type.FLOAT);
		reservedClassTable.put(double.class, Type.DOUBLE);
		reservedClassTable.put(Double.class, Type.DOUBLE);
		reservedClassTable.put(int.class, Type.INT);
		reservedClassTable.put(Integer.class, Type.INT);
		reservedClassTable.put(short.class, Type.SHORT);
		reservedClassTable.put(Short.class, Type.SHORT);
		reservedClassTable.put(boolean.class, Type.BOOL);
		reservedClassTable.put(Boolean.class, Type.BOOL);
		reservedClassTable.put(float.class, Type.FLOAT);
		reservedClassTable.put(Float.class, Type.FLOAT);
		reservedClassTable.put(long.class, Type.LONG);
		reservedClassTable.put(Long.class, Type.LONG);
		reservedClassTable.put(String.class, Type.STRING);
		reservedClassTable.put(StringBuffer.class, Type.STRING);
		reservedClassTable.put(StringBuilder.class, Type.STRING);
		reservedClassTable.put(byte[].class, Type.ARRAY);
		reservedClassTable.put(char[].class, Type.ARRAY);
		reservedClassTable.put(int[].class, Type.ARRAY);
		reservedClassTable.put(short[].class, Type.ARRAY);
		reservedClassTable.put(long[].class, Type.ARRAY);
		reservedClassTable.put(float[].class, Type.ARRAY);
		reservedClassTable.put(double[].class, Type.ARRAY);
	}

	static Type getType(Class clazz) {
		if (reservedClassTable.containsKey(clazz)) {
			return reservedClassTable.get(clazz);
		}
		if (clazz.isArray()) {
			return Type.ARRAY;
		}
		return Type.OBJECT;
	}

	@Override
	public <T> T read(Class<T> clazz, Input in) throws IOException {
		try {	
			T ret = clazz.newInstance();
			if(!(ret instanceof Serializable))
			{
				throw new NotSerializableException(clazz.getName());
			}
			if(ret instanceof Externalizable)
			{
				Externalizable externalizable = (Externalizable) ret;
				externalizable.readExternal(in);
				return ret;
			}
			Field[] fs = ReflectionCache.getDeclaredFields(clazz);
			while (true) {
				int tag = in.readTag();
				if (tag == 0)
					break;

				Field f = fs[tag - 1];
				Class fieldType = f.getType();
				Type t = getType(fieldType);
				switch (t) {
				case BOOL: {
					//f.setBoolean(ret, in.readBool())
					f.set(ret, in.readBool());
					break;
				}
				case CHAR: {
					f.set(ret, in.readChar());
					break;
				}
				case SHORT: {
					f.set(ret, in.readShort());
					break;
				}
				case FLOAT: {
					f.set(ret, in.readFloat());
					break;
				}	
				case DOUBLE: {
					f.set(ret, in.readDouble());
					break;
				}
				case INT: {
					f.set(ret, in.readInt());
					break;
				}
				case LONG: {
					f.set(ret, in.readLong());
					break;
				}
				case STRING: {
					f.set(ret, in.readUTF());
					break;
				}
				case OBJECT: {
					f.set(ret, read(fieldType, in));
					break;
				}
				case ARRAY: {
					int len = in.readInt();
					int index = 0;
					Object array = Array.newInstance(fieldType, len);
					while (index < len) {
						Array.set(array, index, read(fieldType, in));
						index++;
					}
					f.set(ret, array);
					break;
				}
				default:
					break;
				}
			}
			return ret;
		}catch (IOException e) {
			throw e;
		} 
		catch (Exception e) {
			throw new IOException(e);
		}

	}

	@Override
	public void write(Object obj, Output out) throws IOException {
		try {
			Class clazz = obj.getClass();
			if(!(obj instanceof Serializable))
			{
				throw new NotSerializableException(clazz.getName());
			}
			if(obj instanceof Externalizable)
			{
				Externalizable externalizable = (Externalizable) obj;
				externalizable.writeExternal(out);
				return;
			}
			Field[] fs = ReflectionCache.getDeclaredFields(clazz);
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				//f.setAccessible(true);
				Object fieldValue = f.get(obj);
				if (fieldValue != null) {
					out.writeTag(i + 1);
					Class fieldType = f.getType();
					Type t = getType(fieldType);
					switch (t) {
					case BOOL: {
						out.writeBoolean((Boolean) fieldValue);
						break;
					}
					case CHAR: {
						out.writeChar((Character) fieldValue);
						break;
					}
					case SHORT: {
						out.writeShort((Character) fieldValue);
						break;
					}
					case FLOAT: {
						out.writeFloat((Float) fieldValue);
						break;
					}	
					case DOUBLE: {
						out.writeDouble((Double) fieldValue);
						break;
					}
					case INT: {
						out.writeInt((Integer) fieldValue);
						break;
					}
					case LONG: {
						out.writeLong((Long) fieldValue);
						break;
					}
					case STRING: {
						out.writeUTF((String) fieldValue);
						break;
					}
					case OBJECT: {
						write(fieldValue, out);
						break;
					}
					case ARRAY: {
						int len = Array.getLength(fieldValue);
						out.writeInt(len);
						int index = 0;
						while (index < len) {
							write(Array.get(fieldValue, index), out);
							index++;
						}
						break;
					}
					default:
						break;
					}
				}
			}
			out.writeTag(0);
		} catch (Exception e) {
			throw new IOException(e);
		}
		
	}

}
