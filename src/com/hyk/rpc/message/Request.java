/**
 * 
 */
package com.hyk.rpc.message;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class Request extends Message implements Serializable{

	public long getSessionID() {
		return sessionID;
	}
	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}
	public long getObjID() {
		return objID;
	}
	public void setObjID(long objID) {
		this.objID = objID;
	}
	public int getMethodID() {
		return methodID;
	}
	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	private long sessionID;
	private long objID;
	private int methodID;
	private Object[] args;
	
	public Response createResponse()
	{
		Response res = new Response();
		res.setSessionID(sessionID);
		res.address = address;
		return res;
	}
	
}
