/**
 * 
 */
package com.hyk.rpc.serializer;

import java.io.IOException;

/**
 * @author qiying.wang
 *
 */
public interface ObjectSerializer {

	public abstract byte[] mashall(Object obj)throws IOException;
	public abstract Object unmashall(byte[] value)throws IOException, ClassNotFoundException;
	
}
