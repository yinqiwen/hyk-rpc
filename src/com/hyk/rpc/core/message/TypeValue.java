/**
 * 
 */
package com.hyk.rpc.core.message;

import java.io.IOException;

import com.hyk.serializer.Externalizable;
import com.hyk.serializer.HykSerializer.Input;
import com.hyk.serializer.HykSerializer.Output;

/**
 * @author qiying.wang
 *
 */
public class TypeValue implements Externalizable{
	String typeName;
	Object value;
	@Override
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {
		typeName = in.readUTF();
		if(null != typeName)
		{
			value = in.readObject(Class.forName(typeName));
		}
		
	}
	@Override
	public void writeExternal(Output out) throws IOException {
		out.writeUTF(typeName);
		out.writeObject(value);
	}
}
