package com.noklin.potato;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.noklin.potato.exceptions.TestFailedException;

public class TestEntity {

	public static enum Result {
		FAILED, PASSED, ERROR
	}

	private Result result = Result.PASSED;
	private List<Method> beforeMethods = Collections.<Method>emptyList();
	private List<Method> afterMethods = Collections.<Method>emptyList();;
	
	private Class<?> testClass;
	private Method testMethod;
	private Class<? extends Throwable> expectedException;
	private Object instance;
	private Throwable invocationException;
	private String description;

	public Result runTest() {
		try {
			instantiate(); 
			invokeBeforeMethods();
			testMethod.invoke(instance);
			invokeAfterMethods(); 
		} catch (Throwable ex) {
			recognizeException(ex.getCause());
		}
		return result;
	}
	
	private void recognizeException(Throwable ex){
		invocationException = ex;
		Class<?> clazz = ex.getClass();
		if(clazz == TestFailedException.class){
			result = Result.FAILED;
		}else if(clazz != expectedException){
			result = Result.ERROR;
		}
	}

	private void instantiate() throws InstantiationException, IllegalAccessException{
		instance = testClass.newInstance();
	}

	private void invokeBeforeMethods() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for(Method m : beforeMethods){
			m.invoke(instance);
		}
	}
	
	private void invokeAfterMethods() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for(Method m : afterMethods){
			m.invoke(instance);
		}
	}

	public Result getResult() {
		return result;
	}

	public Throwable getInvocationException() {
		return invocationException;
	}

	public void setTestClass(Class<?> testClass) {
		this.testClass = testClass;
	}
	
	public void setTestMethod(Method testMethod) {
		this.testMethod = testMethod;
	}
	
	public void setBeforeMethods(List<Method> beforeMethods) {
		this.beforeMethods = beforeMethods;
	}
	
	public void setAfterMethods(List<Method> afterMethods) {
		this.afterMethods = afterMethods;
	}
	
	public void setExpectedException(Class<? extends Throwable> expectedException) {
		this.expectedException = expectedException;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}