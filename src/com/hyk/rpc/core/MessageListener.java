/**
 * 
 */
package com.hyk.rpc.core;

import com.hyk.rpc.core.message.Message;


/**
 * @author qiying.wang
 *
 */
public interface MessageListener {

	void onMessage(Message msg);
}
