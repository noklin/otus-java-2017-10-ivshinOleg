package com.noklin.potato;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.noklin.potato.annotations.After;
import com.noklin.potato.annotations.Before;
import com.noklin.potato.annotations.Test;

public class TestFinder {

	public List<TestEntity> find(List<String> fullClassNames) throws ClassNotFoundException {
		List<TestEntity> allTests = new ArrayList<>();
		for(String className : fullClassNames){
			allTests.addAll(createTest(Class.forName(className)));
		}
		return allTests;
	}

	public List<TestEntity> find(String packageName) throws ClassNotFoundException {
		return find(findClasses(packageName));
	}
	
	private List<TestEntity> createTest(Class<?> testClass) {
		List<Method> beforeMethods = findMethods(testClass, m -> m.isAnnotationPresent(Before.class));
		List<Method> afterMethods = findMethods(testClass, m -> m.isAnnotationPresent(After.class));
		List<Method> testMethods = findMethods(testClass, m -> m.isAnnotationPresent(Test.class));
		validateSignature(beforeMethods);
		validateSignature(afterMethods);
		validateSignature(testMethods);
		List<TestEntity> tests = new ArrayList<>();
		for(Method m : testMethods){
			TestEntity test = new TestEntity();
			String description = m.getAnnotation(Test.class).description();
			Class<? extends Throwable> expected = m.getAnnotation(Test.class).expected();
			test.setAfterMethods(afterMethods);
			test.setBeforeMethods(beforeMethods);
			test.setTestMethod(m);
			test.setTestClass(testClass);
			test.setDescription(description);
			test.setExpectedException(expected);
			tests.add(test);
		}
		return tests;
	}

	private void validateSignature(List<Method> methods){
		methods.forEach(m -> {
			if(!Modifier.isPublic(m.getModifiers())){
				throw new RuntimeException("Method " + m.getName() + " should be public");
			}
			if(Modifier.isStatic(m.getModifiers())){
				throw new RuntimeException("Method " + m.getName() + " should not be static");
			}
			if(m.getParameterTypes().length != 0){
				throw new RuntimeException("Method " + m.getName() + " should have no parameters");
			}
			if(m.getReturnType() != void.class){
				throw new RuntimeException("Method " + m.getName() + " should be void");
			}
		});
	}
	
	private List<Method> findMethods(Class<?> testClass, Predicate<Method> predicate) {
		List<Method> result = Collections.<Method>emptyList();
		Method[] methods = testClass.getDeclaredMethods();
		for (Method m : methods) {
			if (predicate.test(m)) {
				if (result.isEmpty()) {
					result = new ArrayList<>();
				}
				result.add(m);
			}
		}
		return result;
	}
	
	
	private static final char PKG_SEPARATOR = '.';
	private static final char FILE_SEPARATOR = '/';
    private static final String CLASS_FILE_SUFFIX = ".class";

    public List<String> findClasses(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, FILE_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format("Package is not exist: " + scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<String> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<String> find(File file, String scannedPackage) {
        List<String> classNames = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
            	classNames.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            classNames.add(className);
        }
        return classNames;
    }

}