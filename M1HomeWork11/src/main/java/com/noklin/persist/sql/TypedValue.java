package com.noklin.persist.sql;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

import com.noklin.persist.DataSet;

public class TypedValue{
	
	private final String name;
	private final String tableName;
	private Object value;
	private Type type;
	
	public Type getType() {
		return type;
	}

	public static enum Type {
		RELATION(java.sql.Types.DATALINK), NUMBER(java.sql.Types.INTEGER), 
		STRING(java.sql.Types.VARCHAR), COLLECTION(java.sql.Types.DATALINK);
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
	
	@SuppressWarnings("unchecked")
	public <T> T asSomeType(){
		return (T)value;
	}
	
	@SuppressWarnings("unchecked")
	public final <T> Collection <T> asCollection(){
		return (Collection<T>)value;
	}
	
	public String asString(){
		return String.valueOf(value);
	}

	public String getName() {
		return name + (type == Type.RELATION ? "_id" : "");
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
		return tableName +  "." + getName();
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
			if(value == null){
				if("java.util.Set".equals(className)){
					value = new HashSet<>();
				}else{
					try{
						value = valueClass.newInstance();
					}catch(InstantiationException | IllegalAccessException ex){
						ex.printStackTrace();
					}
				}
			}
			if(DataSet.class.isInstance(value)){
				type = Type.RELATION;
			}else if(Collection.class.isInstance(value)){
				type = Type.COLLECTION;
			}else{
				throw new RuntimeException("Not supported data type: " + className);
			}
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