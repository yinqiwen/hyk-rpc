/**
 * 
 */
package com.hyk.rpc.address;

import java.net.URI;

/**
 * @author qiying.wang
 *
 */
public class HttpUrlRpcAddress extends RpcAddress {

	private URI uri;
	
	public HttpUrlRpcAddress(URI uri)
	{
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;
		HttpUrlRpcAddress other = (HttpUrlRpcAddress) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	
}
