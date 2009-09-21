/**
 * 
 */
package com.hyk.rpc.address;

import java.net.SocketAddress;

/**
 * @author qiying.wang
 *
 */
public class SocketRpcAddress extends RpcAddress {

	private SocketAddress address;
	private SocketRpcAddress(){}
	
	public SocketRpcAddress(SocketAddress address)
	{
		this.address = address;
	}
	
	public SocketAddress getAddress()
	{
		return address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocketRpcAddress other = (SocketRpcAddress) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}
	
	
	
}
