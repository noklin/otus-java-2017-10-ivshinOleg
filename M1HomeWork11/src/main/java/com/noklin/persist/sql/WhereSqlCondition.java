package com.noklin.persist.sql;

public class WhereSqlCondition {
	public enum ConditionType{
		EQUALS;
	}
	private final String name;
	private final ConditionType conditionType;
	public String getName() {
		return name;
	}
	
	public WhereSqlCondition(ConditionType conditionType, String name){
		this.conditionType = conditionType;
		this.name = name;
	}

	public ConditionType getConditionType() {
		return conditionType;
	}
}