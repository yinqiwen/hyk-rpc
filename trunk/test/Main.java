import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Arrays;

import javax.management.ReflectionException;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import com.hyk.rpc.annotation.Remote;

import com.hyk.rpc.util.ClassUtil;




public class Main implements Serializable{

	String s = "asdas";
	long v = 1232142143;
	@Remote
	static interface A
	{
		public int say();
	}
	static interface C extends A
	{
		
	}
	
	static class B implements C{

		@Override
		public int say() {
			// TODO Auto-generated method stub
			return 123;
		}}
	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 * @throws IOException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws ClassNotFoundException 
	 */
	
	private static void writeObj(Object obj, CodedOutputStream cos, int tag) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		Field[] fs = obj.getClass().getDeclaredFields();
		int tagNo = 1;
		for(Field f:fs)
		{
			f.setAccessible(true);
			if(f.getType().isPrimitive())
			{
				Class clazz = f.getType();
				String methodName = "write";
				String typeName = null;
				if(clazz.equals(int.class))
				{
					typeName = "Int32";
				}
				else if(clazz.equals(long.class))
				{
					typeName = "Int64";
				}
				else if(clazz.equals(boolean.class))
				{
					typeName = "Bool";
				}
				else if(clazz.equals(float.class))
				{
					typeName = "Float";
				}
				else if(clazz.equals(double.class))
				{
					typeName = "Double";
				}
				
				Method m = cos.getClass().getMethod(methodName + typeName, new Class[]{int.class, clazz});
				m.invoke(cos, new Object[]{tagNo++, f.get(obj)});
			}
			else if(f.getType().equals(String.class))
			{
				cos.writeString(tagNo++, (String) f.get(obj));
			}
			else
			{
				writeObj(f.get(obj),cos, tagNo++);
			}
			
		}
	}
	
	private static void writeObj(Object obj, CodedOutputStream cos) throws IOException, IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		cos.writeStringNoTag(obj.getClass().getName());
		writeObj(obj,cos,-1);
		cos.flush();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		//ObjectOutputStream oos = new ObjectOutputStream(bos);
		//oos.writeObject(new Main());
		CodedOutputStream cos = CodedOutputStream.newInstance(bos);
		writeObj(new Main(), cos);
		byte[] value = bos.toByteArray();
		System.out.println(value.length);
		//cos
	}

}
