import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.hyk.serializer.StandardSerializer;
import com.hyk.util.buffer.ByteArray;

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
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException {
		// TODO Auto-generated method stub
		TargetClassTop test = new TargetClassTop();
		test.setName("wangqiying!");
		StandardSerializer ser = new StandardSerializer();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			ByteArray array = ser.serialize_(test);
			array.free();
		}
		ByteArray array = ser.serialize_(test);
		long end = System.currentTimeMillis();
		System.out.println("####Serialize time:" + (end - start));
		
		//byte[] data = bos.toByteArray();
		System.out.println("####Serialize size:" + array.size());
		
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			ser.deserialize_(TargetClassTop.class, array);
		}
		ser.deserialize_(TargetClassTop.class, array);
		end = System.currentTimeMillis();
		System.out.println("####Deserialize time:" + (end - start));
	}

}
