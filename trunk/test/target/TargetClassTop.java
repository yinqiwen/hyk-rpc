/**
 * 
 */
package target;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/**
 * @author qiying.wang
 *
 */
public class TargetClassTop implements Serializable, Externalizable{

	public TargetClass getTarget() {
		return target;
	}
	public void setTarget(TargetClass target) {
		this.target = target;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private TargetClass target = new TargetClass();
	private String name = "kgh";
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		target = (TargetClass) in.readObject();
		name = in.readUTF();
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(target);
		out.writeUTF(name);
	}
}
