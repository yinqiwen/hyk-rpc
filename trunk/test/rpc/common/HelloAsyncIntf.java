/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: HelloAsyncIntf.java 
 *
 * @author qiying.wang [ Mar 2, 2010 | 2:17:58 PM ]
 *
 */
package rpc.common;

import com.hyk.rpc.core.RpcCallback;
import com.hyk.rpc.core.annotation.Async;

/**
 *
 */
@Async(HelloIntf.class)
public interface HelloAsyncIntf
{
    public void sayHello(String name, RpcCallback<String> callback);
	
	public void noop(RpcCallback<Void> callback);
}
