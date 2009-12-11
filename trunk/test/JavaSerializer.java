import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import target.TargetClassTop;

/**
 * 
 */

/**
 * @author qiying.wang
 *
 */
public class JavaSerializer {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		TargetClassTop test = new TargetClassTop();
		test.setName("wangqiying!");
		//ObjectOutputStream oos = new ObjectOutputStream(out);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			//byte[] buf = new byte[100];
			ByteArrayOutputStream bos = new ByteArrayOutputStream(208);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(test);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(208);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(test);
		long end = System.currentTimeMillis();
		System.out.println("####Serialize time:" + (end - start));
		
		byte[] data = bos.toByteArray();
		System.out.println("####Serialize size:" + data.length);
		
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			//byte[] buf = new byte[100];
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ios = new ObjectInputStream(bis);
			ios.readObject();
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ios = new ObjectInputStream(bis);
		ios.readObject();
		end = System.currentTimeMillis();
		System.out.println("####Deserialize time:" + (end - start));
	}

}
