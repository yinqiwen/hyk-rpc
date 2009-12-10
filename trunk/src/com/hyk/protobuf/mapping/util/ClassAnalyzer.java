/**
 * 
 */
package com.hyk.protobuf.mapping.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.hyk.protobuf.mapping.data.DataType;
import com.hyk.protobuf.mapping.data.FieldInfo;
import com.hyk.protobuf.mapping.data.MessageInfo;

/**
 * @author qiying.wang
 *
 */
public class ClassAnalyzer {
	
    private static Map<Class, Type> reservedClassTable = new HashMap<Class, Type>();
    private static final String GEN_PKG = "com.hyk.rpc.gen";
	
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
	
	private static Label getLabel(Class clazz)
	{
		if(clazz.isPrimitive() || clazz.isEnum())
		{
			return Label.LABEL_REQUIRED;
		}
		if(clazz.isArray() || List.class.isAssignableFrom(clazz))
		{
			return Label.LABEL_REPEATED;
		}
		
		return Label.LABEL_OPTIONAL;
	}
	
	public List<MessageInfo> analyze(Class clazz)
	{
		List<MessageInfo> ret = new LinkedList<MessageInfo>();
		MessageInfo info = new MessageInfo();
		info.type = DataType.wrap(clazz);
		//info.simpleName = clazz.getSimpleName();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			Class type = f.getType();
			DataType dataType = DataType.wrap(type);
			FieldInfo fi = new FieldInfo();
			fi.name = f.getName();
			fi.tag = i + 1;
			fi.type = dataType;
			info.fields.add(fi);
			
			if(!dataType.isReserved())
			{
				List<MessageInfo> temp = analyze(type);
				ret.addAll(temp);
			}		
		}
		ret.add(info);
		return ret;
	}
	
	private FileDescriptorProto.Builder getFileDescriptorProtoBuilder(List<FileDescriptorProto.Builder> buffer, Class clazz)
	{
		String simpleName = clazz.getSimpleName();
		String packageName = clazz.getPackage() == null?GEN_PKG:GEN_PKG + "." + clazz.getPackage().getName();

		for (FileDescriptorProto.Builder fileDescriptorProto : buffer) {
			FileOptions options = fileDescriptorProto.getOptions();
			if(fileDescriptorProto.getOptions().getJavaPackage().equals(packageName)
					&& fileDescriptorProto.getOptions().getJavaOuterClassname().equals("Gen"))
			{
				return fileDescriptorProto;
			}
		}
		
		FileDescriptorProto.Builder fileProtoBuilder  = FileDescriptorProto.newBuilder();
		FileOptions.Builder options = FileOptions.newBuilder().setJavaPackage(packageName).setJavaOuterClassname("Gen");
		fileProtoBuilder.setOptions(options);
		buffer.add(fileProtoBuilder);
		return fileProtoBuilder;
	}
	
	public DescriptorProto getDescriptorProto(FileDescriptorProto.Builder fileDescriptorProto, Class clazz)
	{
		List<DescriptorProto> messageList = fileDescriptorProto.getMessageTypeList();
		for (DescriptorProto descriptorProto : messageList) {
			if(descriptorProto.getName().equals(clazz.getSimpleName()))
			{
				return descriptorProto;
			}
		}
		
		return null;
	}
	
	
	
	
	public DescriptorProto parse(Class clazz, List<FileDescriptorProto.Builder> buffer)
	{
		
		FileDescriptorProto.Builder fileDescriptorProto = getFileDescriptorProtoBuilder(buffer, clazz);

		DescriptorProto descriptorProto = getDescriptorProto(fileDescriptorProto, clazz);
		if(null != descriptorProto)
		{
			return descriptorProto;
		}
		
		if(clazz.isEnum())
		{
			
		}
		else
		{
			
		}
		DescriptorProto.Builder messageProtoBuilder = DescriptorProto.newBuilder();
		messageProtoBuilder.setName(clazz.getSimpleName());
		
		//messageProtoBuilder.setf
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			int modifier = f.getModifiers();
			if(Modifier.isStatic(modifier) 
					|| Modifier.isTransient(modifier))
			{
				continue;
			}
			//f
			FieldDescriptorProto.Builder fieldProtoBuilder = FieldDescriptorProto.newBuilder();
			
			Type type = getType(f.getType());
			fieldProtoBuilder.setType(type);
			
			fieldProtoBuilder.setName(f.getName());
			fieldProtoBuilder.setNumber(i+1);
			fieldProtoBuilder.setLabel(getLabel(f.getType()));
			
			if(type.equals(Type.TYPE_ENUM) || type.equals(Type.TYPE_MESSAGE))
			{			
				DescriptorProto fieldDesc = parse(f.getType(), buffer);
				fieldProtoBuilder.setTypeName(fieldDesc.getName());
			}
			else
			{
				fieldProtoBuilder.setTypeName(ObjectRender.renderType(type));
			}
			
			messageProtoBuilder.addField(fieldProtoBuilder);
		}
		descriptorProto = messageProtoBuilder.build();
		fileDescriptorProto.addMessageType(descriptorProto);
		//return null;
		return descriptorProto;
	}
	
	public String getJavaTargetPackageName(FileDescriptorProto file)
	{
		String packageName = file.getOptions().getJavaPackage();
		String rest = packageName.substring(GEN_PKG.length());
		return null;
	}
	
	public static void main(String[] args)
	{
		System.out.println(Type.TYPE_BOOL);
	}
	
}
