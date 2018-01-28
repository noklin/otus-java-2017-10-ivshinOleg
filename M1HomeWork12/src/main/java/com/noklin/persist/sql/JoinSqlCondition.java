package com.noklin.persist.sql;

public class JoinSqlCondition {
	private final String leftColumnName;
	private final String rightColumnName;
	private final String tableName;
	public JoinSqlCondition(String leftColumnName, String rightColumnName, String tableName) {
		this.leftColumnName = leftColumnName;
		this.rightColumnName = rightColumnName;
		this.tableName = tableName;
	}
	public String getLeftColumnName() {
		return leftColumnName;
	}
	public String getRightColumnName() {
		return rightColumnName;
	}
	public String getTableName() {
		return tableName;
	}
}