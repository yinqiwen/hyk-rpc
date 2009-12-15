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
public class Request implements Externalizable{

	private long sessionID;
	private long objID;
	private int operationID;
	
	private String[] argTypes;
	private Object[] args;
	
	@Override
	public void readExternal(Input in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void writeExternal(Output out) throws IOException {
		out.writeLong(sessionID);
		
	}
	
	
}
