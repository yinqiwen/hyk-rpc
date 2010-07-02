package com.hyk.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import junit.framework.TestCase;

import com.hyk.serializer.reflect.ReflectionCache;

public class HykSerializerTest2 extends TestCase {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException {
		Field[] fs = ReflectionCache.getSerializableFields(NullPointerException.class);
		System.out.println(Arrays.toString(fs));
	}

	
}
