/**
 * 
 */
package com.hyk.serializer;

import java.io.IOException;
import java.io.NotSerializableException;

import com.hyk.io.ByteDataBuffer;
import com.hyk.util.buffer.ByteArray;
import com.hyk.util.buffer.ByteArrayPool;

/**
 * @author Administrator
 *
 */
public interface Serializer {
	
	ByteDataBuffer serialize(Object value) throws NotSerializableException, IOException ;
	ByteDataBuffer serialize(Object value, ByteDataBuffer data) throws NotSerializableException, IOException ;
	
	<T> T deserialize(Class<T> type,ByteDataBuffer data) throws NotSerializableException, IOException,InstantiationException ;
}
