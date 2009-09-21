/**
 * 
 */
package com.hyk.rpc.message;

import java.io.Serializable;
import java.net.SocketAddress;
import java.net.URI;

import com.hyk.rpc.address.RpcAddress;


/**
 * @author Administrator
 *
 */
public abstract class Message implements Serializable{

	public transient RpcAddress address;
}
