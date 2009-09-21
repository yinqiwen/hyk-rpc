/**
 * 
 */
package com.hyk.rpc.message;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class Response extends Message implements Serializable{

	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Object getReturnObj() {
		return returnObj;
	}
	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	private boolean isSuccess;
	private Object returnObj;
	private String extra;
	private long sessionID;
	public long getSessionID() {
		return sessionID;
	}
	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}
}
