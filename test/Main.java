import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//import net.sf.cglib.proxy.Callback;
//import net.sf.cglib.proxy.Enhancer;





public class Main {


	static interface A
	{
		
	}
	static interface C extends A
	{
		
	}
	
	static class B implements C{}
	static class TestHandlers implements InvocationHandler,Externalizable 
	{
		int k;
		public TestHandlers(){}
		public TestHandlers(int k){this.k = k;}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			// TODO Auto-generated method stub
			System.out.println("callle!" + in.readChar() + in.readChar());
		}

		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			// TODO Auto-generated method stub
			out.writeChars("hello");
			System.out.println("callle2!");
			
		}
		
	}
	
	public void sayHello()
	{
		System.out.println("hello");
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException {
		ServerSocketChannel.open().socket().bind(new InetSocketAddress(48100), 50);
		SocketChannel.open().socket().bind(new InetSocketAddress(48100));
	}

}
