package com.noklin.assertions;

import com.noklin.potato.exceptions.TestFailedException;

public class Assert {

	public static void assertTrue(boolean asert){ 
		if(!asert) throw new TestFailedException();
	}
}
