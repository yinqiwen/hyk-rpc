/**
 * 
 */
package com.hyk.rpc.core.transport;

import com.hyk.io.ByteDataBuffer;
import com.hyk.rpc.core.address.Address;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class RpcChannelData {

	public RpcChannelData(ByteDataBuffer data, Address address) {
		this.content = data;
		this.address = address;
	}
	public final ByteDataBuffer content;
	public final Address address;
}
