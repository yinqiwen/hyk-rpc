/**
 * 
 */
package com.hyk.serializer;

import java.io.IOException;

/**
 * @author Administrator
 *
 */
public interface Serializer {

	byte[] serialize(Object obj) throws java.io.NotSerializableException, IOException ;
	
	<T> T deserialize(Class<T> type,byte[] data) throws java.io.NotSerializableException, IOException ;
}
