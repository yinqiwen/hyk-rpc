import java.io.IOException;
import java.nio.ByteBuffer;

import target.TargetClassTop;

import com.hyk.io.buffer.ChannelDataBuffer;
import com.hyk.serializer.StandardSerializer;

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
			ChannelDataBuffer array = ser.serialize(test);
			//array.free();
		}
		ChannelDataBuffer array = ser.serialize(test);
		long end = System.currentTimeMillis();
		System.out.println("####Serialize time:" + (end - start));
		
		//byte[] data = bos.toByteArray();
		System.out.println("####Serialize size:" + array.readableBytes());
		ByteBuffer[] bufs = ChannelDataBuffer.asByteBuffers(array);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 99999; i++) {
			ser.deserialize(TargetClassTop.class, array);
		}
		for(ByteBuffer buf:bufs)
		{
			System.out.println("####" + buf);
		}
		ser.deserialize(TargetClassTop.class, array);
		end = System.currentTimeMillis();
		System.out.println("####Deserialize time:" + (end - start));
	}

}
