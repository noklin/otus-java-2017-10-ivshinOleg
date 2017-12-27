package com.noklin;

public class Launcher {

	static class A {
		int a = 0;
		boolean bool;
		Object[] obj = { 1, 2, 3, 4, new Object[] { new Object[] { 1, 2, 3, 4, new Object[] {} } } };
	}

	public static void main(String[] args) {
		System.out.println(new Jsonizer().toJsonValue(new A()));
	}
}