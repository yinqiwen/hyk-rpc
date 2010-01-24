/**
 * 
 */
package com.hyk.rpc.core.util;

import com.hyk.rpc.core.session.SessionManager;

/**
 * @author Administrator
 * 
 */
public class CommonUtil {

//	private static ThreadLocal<SessionManager> rpcInstanceTable = new ThreadLocal<SessionManager>();
//
//	public static SessionManager getSessionManager() {
//		return rpcInstanceTable.get();
//	}
//
//	public static void setSessionManager(SessionManager rpc) {
//		rpcInstanceTable.set(rpc);
//	}

//	public static TypeValue[] buildTypeValues(Object[] args) {
//		if (null != args) {
//			TypeValue[] ret = new TypeValue[args.length];
//			for (int i = 0; i < ret.length; i++) {
//				ret[i] = new TypeValue();
//				ret[i].setTypeName(args[i].getClass().getName());
//				ret[i].setValue(args[i]);
//			}
//			return ret;
//		}
//		return null;
//	}
//
//	public static TypeValue buildTypeValue(Object arg) {
//		if (null != arg) {
//			TypeValue ret = new TypeValue();
//			ret.setTypeName(arg.getClass().getName());
//			ret.setValue(arg);
//			return ret;
//		}
//		return null;
//	}
}
