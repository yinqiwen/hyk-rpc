/**
 * 
 */
package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;

/**
 * @author Administrator
 *
 */
public interface Serializer {

	byte[] serialize(Object obj) throws NotSerializableException, IOException ;
	
	<T> T deserialize(Class<T> type,byte[] data) throws NotSerializableException, IOException,InstantiationException ;
}
