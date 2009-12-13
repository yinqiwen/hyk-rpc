/**
 * 
 */
package com.hyk.serializer.util;

import java.io.IOException;


import com.hyk.serializer.HykSerializer.Input;
import com.hyk.serializer.HykSerializer.Output;

/**
 * @author Administrator
 *
 */
public interface ObjectIOHandler {

	void write(Object obj, Output output) throws IOException;
	
	<T> T read(Class<T> clazz, Input input) throws IOException;
}
