/**
 * 
 */
package com.hyk.serializer.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiying.wang
 *
 */
public class ObjectIOHandlerFactory {

	private static Map<Class, ObjectIOHandler> objectIOHandlerTable = new HashMap<Class, ObjectIOHandler>();
	private static ObjectIOHandler reflectObjectIOHandlerInstatnce = new ReflectObjectIOHandler2();
	
	public static ObjectIOHandler getObjectIOHandler(Class clazz)
	{
		ObjectIOHandler ret = objectIOHandlerTable.get(clazz);
		if(null == ret)
		{
			ret = reflectObjectIOHandlerInstatnce;
		}
		return ret;
	}
}
