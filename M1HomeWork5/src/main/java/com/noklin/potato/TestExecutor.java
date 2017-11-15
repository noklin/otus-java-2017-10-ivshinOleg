package com.noklin.potato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExecutor {

	private Map<TestEntity.Result,List<TestEntity>> executedTests = new HashMap<>();
	private TestExecutor(){
		executedTests.put(TestEntity.Result.ERROR, new ArrayList<>());
		executedTests.put(TestEntity.Result.FAILED, new ArrayList<>());
		executedTests.put(TestEntity.Result.PASSED, new ArrayList<>());
	}
	
	/**
	 * Execute tests.
	 * @param fullClassNames - full test class names
	 * @throws ClassNotFoundException 
	 * */
	public static void execute(List<String> classes) throws ClassNotFoundException{
		new TestExecutor().executeTests(new TestFinder().find(classes));
	}
	
	/**
	 * Execute tests.
	 * @param packageName - test tocation
	 * @throws ClassNotFoundException 
	 * */
	public static void execute(String packageName) throws ClassNotFoundException{
		new TestExecutor().executeTests(new TestFinder().find(packageName));
	}

	private int executed;
	private void executeTests(List<TestEntity> tests){
		tests.forEach(t ->{
			executedTests.get(t.runTest()).add(t);
			executed++;
		});
		printResult();
	}
	
	private void printResult(){
		int passed = executedTests.get(TestEntity.Result.PASSED).size();
		int failed = executedTests.get(TestEntity.Result.FAILED).size();
		int errors = executedTests.get(TestEntity.Result.ERROR).size();
		System.out.printf("Result: Executed: %d, Passed: %d, Failed: %d, Errors: %d%n", executed, passed, failed, errors);
		if(failed > 0){
			printFailureInfo();
		}
		
		if(errors > 0){
			printErrorInfo();
		}
	}
	
	private void printErrorInfo(){
		System.out.println("Tests with error:");
		executedTests.get(TestEntity.Result.ERROR).forEach(t -> t.getInvocationException().printStackTrace(System.out));
	}

	private void printFailureInfo(){
		System.out.println("Failed tests:");
		executedTests.get(TestEntity.Result.FAILED).forEach(t -> {
			System.out.println(t.getDescription());
			t.getInvocationException().printStackTrace(System.out);
		});
	}
}