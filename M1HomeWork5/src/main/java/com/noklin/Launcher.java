package com.noklin;

import java.io.IOException;
import java.util.Arrays;

import com.noklin.potato.TestExecutor;
import com.noklin.potato.annotations.After;
import com.noklin.potato.annotations.Before;
import com.noklin.potato.annotations.Test;

public class Launcher{
	
	public static void main(String[] args) throws SecurityException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{ 
		TestExecutor.execute("com.noklin");
		TestExecutor.execute(Arrays.asList("com.noklin.Launcher"));
	}

	private int count;
	@Before
	public void before(){
		count++;
	}

	@After
	public void after(){
		count--;
	}

	@Test
	public void testWithError(){
		throw new RuntimeException();
	}
	
	@Test(expected = IllegalStateException.class)
	public void successsTestWithException(){
		throw new IllegalStateException();
	}

	@Test
	public void successsTest(){
		com.noklin.assertions.Assert.assertTrue(count == 1);
	}

	@Test(description = "Some description.")
	public void failedTest(){
		com.noklin.assertions.Assert.assertTrue(count == 3);
	}
	
}