package com.hyk.serializer.util;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.hyk.serializer.Externalizable;
import com.hyk.serializer.HykSerializer.Input;
import com.hyk.serializer.HykSerializer.Output;

public class ReflectObjectIOHandler2 implements ObjectIOHandler {

	static enum Type {
		BOOL(0), BYTE(1), INT(2), LONG(3), FLOAT(4), DOUBLE(5), SHORT(6), CHAR(
				7), STRING(8), ARRAY(9), OBJECT(10), ENUM(11), PROXY(12);

		private int value;

		private Type(int value) {
			this.value = value;
		}

		public final int getValue() {
			return value;
		}
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

	ReflectObjectIOHandler2(){}
	
	static Type getType(Class clazz) {
		if (reservedClassTable.containsKey(clazz)) {
			return reservedClassTable.get(clazz);
		}
		Type ret = null;
		if (clazz.isArray()) {
			ret = Type.ARRAY;
		}
		else if (clazz.isEnum()) {
			ret = Type.ENUM;
		}
		else if (Proxy.isProxyClass(clazz)) {
			ret = Type.PROXY;
		}
		else{
			ret = Type.OBJECT;
		}
		reservedClassTable.put(clazz, ret);
		return ret;
	}

	@Override
	public <T> T read(Class<T> clazz, Input in) throws IOException {
		try {
			if(in.available() == 0) return null;
			Type t = getType(clazz);
			switch (t) {
			case BOOL: {
				Boolean b = in.readBool();
				return (T) b;
			}
			case BYTE: {
				Byte b = in.readByte();
				return (T) b;
			}
			case CHAR: {
				Character c = in.readChar();
				return (T) c;
			}
			case SHORT: {
				Short s = in.readShort();
				return (T) s;
			}
			case FLOAT: {
				Float f = in.readFloat();
				return (T) f;
			}
			case DOUBLE: {
				Double d = in.readDouble();
				return (T) d;
			}
			case INT: {
				Integer i = in.readInt();
				return (T) i;
			}
			case LONG: {
				Long l = in.readLong();
				return (T) l;
			}
			case STRING: {
				String s = in.readUTF();
				return (T) s;
			}
			case ENUM:
			{
				String name = in.readUTF();
				Class cls = clazz;
				return (T) Enum.valueOf(cls, name);
			}
			case PROXY:
			{			
				ClassLoader loader = clazz.getClassLoader();
				String[] interfaceNames = read(String[].class, in);
				Class[] interfaces = new Class[interfaceNames.length];
				for (int i = 0; i < interfaceNames.length; i++) {
					interfaces[i] = Class.forName(interfaceNames[i], true, loader);
				}
				Class proxyHandlerClass = Class.forName(in.readUTF(), true, loader);
				InvocationHandler handler = (InvocationHandler) read(proxyHandlerClass, in);
				return (T) Proxy.newProxyInstance(loader, interfaces, handler);
			}
			case OBJECT: {
				Constructor<T> cons = ReflectionCache.getDefaultConstructor(clazz);
				T ret = cons.newInstance(null);
				if (!(ret instanceof Serializable)) {
					throw new NotSerializableException(clazz.getName());
				}
				if (ret instanceof Externalizable) {
					Externalizable externalizable = (Externalizable) ret;
					externalizable.readExternal(in);
					return ret;
				}
				Field[] fs = ReflectionCache.getSerializableFields(clazz);
				while (true) {
					int tag = in.readTag();
					if (tag == 0)
						break;
					Field f = fs[tag - 1];
					Class fieldType = f.getType();
					f.set(ret, read(fieldType, in));
				}
				return ret;
			}
			case ARRAY: {
				int len = in.readInt();
				int index = 0;
				Object array = Array.newInstance(clazz.getComponentType(), len);
				while (index < len) {
					Array.set(array, index, read(clazz.getComponentType(), in));
					index++;
				}
				return (T) array;
			}
			default: {
				throw new IOException("Unsupport type:" + t);
			}
			}

		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}

	}

	@Override
	public void write(Object obj, Output out) throws IOException {
		try {
			if (null == obj)
				return;
			Class clazz = obj.getClass();
			Type t = getType(clazz);
			switch (t) {
			case BOOL: {
				out.writeBoolean((Boolean) obj);
				break;
			}
			case BYTE: {
				out.writeByte((Byte) obj);
				break;
			}
			case CHAR: {
				out.writeChar((Character) obj);
				break;
			}
			case SHORT: {
				out.writeShort((Character) obj);
				break;
			}
			case FLOAT: {
				out.writeFloat((Float) obj);
				break;
			}
			case DOUBLE: {
				out.writeDouble((Double) obj);
				break;
			}
			case INT: {
				out.writeInt((Integer) obj);
				break;
			}
			case LONG: {
				out.writeLong((Long) obj);
				break;
			}
			case STRING: {
				out.writeUTF((String) obj);
				break;
			}
			case ENUM:
			{
				Enum e = (Enum) obj;
				out.writeUTF(e.name());
				break;
			}
			case PROXY:
			{
				Class[] interfaces = clazz.getInterfaces();
				String[] interfaceNames = new String[interfaces.length];
				for (int i = 0; i < interfaceNames.length; i++) {
					interfaceNames[i] = interfaces[i].getName();
				}
				write(interfaceNames,out);
				InvocationHandler handler = Proxy.getInvocationHandler(obj);
				out.writeUTF(handler.getClass().getName());
				write(handler,out);
				break;
			}
			case OBJECT: {
				if (!(obj instanceof Serializable)) {
					throw new NotSerializableException(clazz.getName());
				}
				if (obj instanceof Externalizable) {
					Externalizable externalizable = (Externalizable) obj;
					externalizable.writeExternal(out);
					return;
				}
				Field[] fs = ReflectionCache.getSerializableFields(clazz);
				for (int i = 0; i < fs.length; i++) {
					Field f = fs[i];
					
					Object fieldValue = f.get(obj);
					if(null != fieldValue)
					{
						out.writeTag(i + 1);
						write(fieldValue, out);
					}		
				}
				out.writeTag(0);
				break;
			}
			case ARRAY: {
				int len = Array.getLength(obj);
				out.writeInt(len);
				int index = 0;
				while (index < len) {
					write(Array.get(obj, index), out);
					index++;
				}
				break;
			}
			default:
				break;
			}
		} catch (Exception e) {
			throw new IOException(e);
		}

	}

}
