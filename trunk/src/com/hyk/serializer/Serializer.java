/**
 * 
 */
package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.util.buffer.ByteArray;
import com.hyk.util.buffer.ByteArrayPool;

/**
 * @author Administrator
 *
 */
public interface Serializer {

	public static final ByteArrayPool pool = new ByteArrayPool();
	
	ByteArray serialize(Object obj) throws NotSerializableException, IOException ;
	
	<T> T deserialize(Class<T> type,ByteArray data) throws NotSerializableException, IOException,InstantiationException ;
}
