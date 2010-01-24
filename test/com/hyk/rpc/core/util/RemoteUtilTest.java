package com.hyk.rpc.core.util;

import com.hyk.rpc.core.remote.Remote;
import com.hyk.rpc.core.service.NameService;
import com.hyk.rpc.core.service.NameServiceImpl;

import junit.framework.TestCase;

public class RemoteUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetRemoteInterfaces() {
		System.out.println(NameService.class.isAnnotationPresent(Remote.class));
	}

}
