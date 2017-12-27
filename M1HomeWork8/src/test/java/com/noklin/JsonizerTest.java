package com.noklin;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;

public class JsonizerTest {
	@Test
	public void test0(){ 
		SomeClass src = new SomeClass();
		src.init();
		String json = new Jsonizer().toJsonValue(src).toString(); 
		SomeClass dest = new Gson().fromJson(json, SomeClass.class);  
		assertEquals(src,dest);
	}
}
