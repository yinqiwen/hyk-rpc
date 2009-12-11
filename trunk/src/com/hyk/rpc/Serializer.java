/**
 * 
 */
package com.hyk.rpc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import target.TargetClassTop;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;

/**
 * @author qiying.wang
 *
 */
public class Serializer {

	private static Map<Class, Type> reservedClassTable = new HashMap<Class, Type>();
	static
	{
		reservedClassTable.put(int.class, Type.TYPE_INT32);
		reservedClassTable.put(Integer.class, Type.TYPE_INT32);
		reservedClassTable.put(short.class, Type.TYPE_INT32);
		reservedClassTable.put(Short.class, Type.TYPE_INT32);
		reservedClassTable.put(boolean.class, Type.TYPE_BOOL);
		reservedClassTable.put(Boolean.class, Type.TYPE_BOOL);
		reservedClassTable.put(float.class, Type.TYPE_FLOAT);
		reservedClassTable.put(Float.class, Type.TYPE_FLOAT);
		reservedClassTable.put(long.class, Type.TYPE_INT64);
		reservedClassTable.put(Long.class, Type.TYPE_INT64);
		reservedClassTable.put(String.class, Type.TYPE_STRING);
		reservedClassTable.put(StringBuffer.class, Type.TYPE_STRING);
		reservedClassTable.put(StringBuilder.class, Type.TYPE_STRING);
		reservedClassTable.put(byte[].class, Type.TYPE_BYTES);
	}
	
	private static Type getType(Class clazz)
	{
		if(reservedClassTable.containsKey(clazz))
		{
			return reservedClassTable.get(clazz);
		}
		if(clazz.isEnum())
		{
			return Type.TYPE_ENUM;
		}
		return Type.TYPE_MESSAGE;
	}
	
	protected static int getSerializedSize(Object o) throws IllegalArgumentException, IllegalAccessException
	{
		int size = CodedOutputStream.computeTagSize(0);
		Class clazz = o.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];			
			f.setAccessible(true);
			Object fieldValue = f.get(o);
			if(fieldValue != null)
			{
				size += CodedOutputStream.computeTagSize(i + 1);
				Class fieldType = f.getType();
				Type t = getType(fieldType);
				switch (t) {
				case TYPE_BOOL:
				{
					size += CodedOutputStream.computeBoolSizeNoTag((Boolean)fieldValue);
					break;
				}
				case TYPE_INT32:
				{
					size += CodedOutputStream.computeInt32SizeNoTag((Integer)fieldValue);
					break;
				}
				case TYPE_INT64:
				{
					size += CodedOutputStream.computeInt64SizeNoTag((Long)fieldValue);
					break;
				}
				case TYPE_STRING:
				{
					size += CodedOutputStream.computeStringSizeNoTag((String) fieldValue);
					break;
				}
				case TYPE_MESSAGE:   
				{
					size += getSerializedSize(fieldValue);
					//size += CodedOutputStream.computeTagSize(-1);
					break;
				}
				default:
					break;
				}				
			}
		}
		
		return size;
	}
	
	protected static void writeTo(CodedOutputStream out, Object o) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		Class clazz = o.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];			
			f.setAccessible(true);
			Object fieldValue = f.get(o);
			if(fieldValue != null)
			{
				//System.out.println("###" + (i + 1));
				//size += CodedOutputStream.computeTagSize(i + 1);
				Class fieldType = f.getType();
				Type t = getType(fieldType);
				switch (t) {
				case TYPE_BOOL:
				{
					out.writeBool(i+1, (Boolean) fieldValue);
					break;
				}
				case TYPE_INT32:
				{
					out.writeInt32(i+1, (Integer) fieldValue);
					break;
				}
				case TYPE_INT64:
				{
					out.writeInt64(i+1, (Long) fieldValue);
					break;
				}
				case TYPE_STRING:
				{
					out.writeString(i+1, (String) fieldValue);
					break;
				}
				case TYPE_MESSAGE:   
				{
					out.writeTag(i+1, 0);
					writeTo(out, fieldValue);
					break;
				}
				default:
					break;
				}				
			}
		}
		out.writeTag(0, 0);
	}
	
	protected static int readTag(CodedInputStream in) throws IOException {
		if (in.isAtEnd()) {

			return 0;
		}

		int lastTag = in.readRawVarint32();

		return lastTag;
	}
	
	protected static void readFrom(CodedInputStream in, Object o) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		Field[] fs = o.getClass().getDeclaredFields();
		while(true)
		{
			int tag = readTag(in);
			if(tag == 0 || tag == 1000) return;
			//if(tag > fs.length) return;
			tag = WireFormat.getTagFieldNumber(tag);
			if(tag == 0) return;
			//System.out.println("???" + tag);
			Field f = fs[tag-1];
			f.setAccessible(true);
			Class fieldType = f.getType();
			Type t = getType(fieldType);
			switch (t) {
			case TYPE_BOOL:
			{
				f.set(o, in.readBool());
				break;
			}
			case TYPE_INT32:
			{
				f.set(o, in.readInt32());
				break;
			}
			case TYPE_INT64:
			{
				f.set(o, in.readInt64());
				break;
			}
			case TYPE_STRING:
			{
				String s = in.readString();
				//System.out.println("####" + s);
				f.set(o, s);
				break;
			}
			case TYPE_MESSAGE:   
			{
				Object fo = fieldType.newInstance();
				f.set(o, fo);
				readFrom(in, fo);
				break;
			}
			default:
				break;
			}	
		}

	}
	
	public static Object parse(Class clazz, byte[] data)
	{
		try {
			Object ret = clazz.newInstance();
			CodedInputStream in = CodedInputStream.newInstance(data);
			readFrom(in, ret);
			return ret;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] serialize(Object o)
	{
		try {
			  //Class clazz = o.getClass();
		      final byte[] result = new byte[getSerializedSize(o)];
		      //System.out.println("###size is " + result.length);
		      final CodedOutputStream output = CodedOutputStream.newInstance(result);
		      writeTo(output, o);
		      output.checkNoSpaceLeft();
              return result;
		    }  catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	
	public static void main(String[] args)
	{
		//System.out.println("###0 size is " + CodedOutputStream.computeTagSize(0));
		TargetClassTop test = new TargetClassTop();
		test.setName("wangqiying!");
		long start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			serialize(test);
		}
		byte[] data = serialize(test);
		long end = System.currentTimeMillis();
		System.out.println("####Serialize time:" + (end - start));
		System.out.println("####Serialize size:" + data.length);
		start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			parse(TargetClassTop.class, data);
		}
		end = System.currentTimeMillis();
		TargetClassTop got = (TargetClassTop) parse(TargetClassTop.class, data);
		System.out.println("####Deserialize time:" + (end - start));
		System.out.println(got.getName());
	}
}
