package com.noklin.persist.sql;

import java.lang.reflect.Field;

public class TypedValue {
	
	private final String name;
	private final String tableName;
	private Object value;
	private Type type;
	
	public Type getType() {
		return type;
	}

	public static enum Type {
		RELATION(java.sql.Types.DATALINK), NUMBER(java.sql.Types.INTEGER), STRING(java.sql.Types.VARCHAR);
		private final int sqlType;
		Type(int sqlType){
			this.sqlType = sqlType;
		}
		public int getSqlType() {
			return sqlType;
		}
	}
	
	public Number asNumber(){
		return (Number)value;
	}
	
	public String asString(){
		return String.valueOf(value);
	}

	public String getName() {
		return name;
	}

	public boolean isNull(){
		return value == null;
	}
	
	public String getTableName() {
		return tableName;
	}

	TypedValue(String tableName, String name, Field field, Object obj) throws IllegalArgumentException, IllegalAccessException{
		this(tableName, name, field.getType(), field.get(obj));
	}
	
	Object getValue(){
		return value;
	}
	
	public TypedValue(String tableName, String name, Class<?> valueClass, Object value){
		this.name = name;
		this.value = value;
		this.tableName = tableName;
		recognizeType(valueClass);
	}


	String getFullName(){
		return tableName +  "." + name;
	}
	
	private void recognizeType(Class<?> valueClass){
		String className = valueClass.getName();
		switch(className){
		case "long":
		case "int":
		case "java.lang.Long":
			type = Type.NUMBER;
			break;
		case "java.lang.String":
			type = Type.STRING;
			break;
		default:
			type = Type.RELATION;
		}
	}
	
	@Override
	public String toString() {
		return type + "{" + name +":" + value + "}";
	}
	
	void setValue(Object value){
		this.value = value;
	}
	
}