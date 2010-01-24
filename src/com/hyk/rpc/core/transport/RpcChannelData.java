/**
 * 
 */
package com.hyk.rpc.core.transport;

import com.hyk.rpc.core.address.Address;
import com.hyk.util.buffer.ByteArray;

/**
 * @author Administrator
 *
 */
public class RpcChannelData {

	public RpcChannelData(ByteArray data, Address address) {
		this.content = data;
		this.address = address;
	}
	public ByteArray content;
	public Address address;
}
