package com.noklin;

import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class MyArrayListTest {

	private MyArrayList<Integer> list; 
	
	@Before
	public void init(){
		list = new MyArrayList<>();
	}
	
	@Test
	public void testAdd(){
		int i = 100;
		while(i --> 0)
			list.add(i);
	}

	@Test
	public void testSize(){
		int i = 100;
		while(i --> 0)
			list.add(i); 
		assertTrue(list.size() == 100);
	}
	
	@Test
	public void testContains(){
		int i = 100;
		while(i --> 0)
			list.add(i); 
		assertTrue(list.size() == 100);
	}

	@Test
	public void testAddAll(){
		list.addAll(Arrays.asList(1, 2)); 
	}
	
	@Test
	public void testCopy(){
		Integer one = 1;
		Integer two = 2;
		Integer three = 3;
		list.add(one);
		list.add(two);
		list.add(three);
		MyArrayList<Integer> des = new MyArrayList<>();
		des.add(new Integer(1));
		des.add(new Integer(2));
		des.add(new Integer(3));
		Collections.copy(des, list);
		assertTrue(des.get(0) == one && des.get(1) == two && des.get(2) == three);
	}
	
	@Test
	public void testSort(){
		Integer one = 1;
		Integer two = 2;
		Integer three = 3;
		list.add(two);
		list.add(three);
		list.add(one);
		Collections.sort(list, (l,r) -> l-r);
		assertTrue(list.get(0) == one && list.get(1) == two && list.get(2) == three);
	}
	
}