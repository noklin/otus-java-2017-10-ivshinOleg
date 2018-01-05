package com.noklin.persist.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Table;

import com.noklin.persist.DataSet;

class EntityModel<T extends DataSet> {
	
	private String tableName;
	private List<TypedValue> fields = new ArrayList<>(); 
	private T dataSet;
	
	EntityModel(T dataSet){
		this.dataSet = dataSet;
		recognizeTableName();
		recognizeColumns();
	}

	private void recognizeColumns() {
		try{
			fields.add(new TypedValue(tableName, DataSet.ID, long.class, dataSet.getId()));
			for(Field field : dataSet.getClass().getDeclaredFields()){
				field.setAccessible(true);
				if(!transientTest(field)){
					fields.add(new TypedValue(tableName, field.getName(), field, dataSet));
				}
			}
		}catch(IllegalAccessException ex){
			ex.printStackTrace();
		}
		
	}
	
	private boolean transientTest(Field field) {
		return (Modifier.TRANSIENT & field.getModifiers()) != 0;
	}

	private void recognizeTableName() {
		Class<?> entityClass = dataSet.getClass();
		if(entityClass.isAnnotationPresent(Table.class)){
			tableName = entityClass.getAnnotation(Table.class).name();
		}else{
			tableName = dataSet.getClass().getSimpleName().toLowerCase();
		} 
	}

	String getTableName() {
		return tableName;
	}

	List<TypedValue> getFields() {
		return fields;
	}
	
	List<JoinSqlCondition> getJoinConditions(){
		return Collections.emptyList();
	}
	
	T constructEntity(){
		fields.forEach(val -> {
			try{
				if(!DataSet.ID.equals(val.getName())){
					Field field = dataSet.getClass().getDeclaredField(val.getName());
					field.setAccessible(true);
					field.set(dataSet, val.getValue());
				}
			}catch(NoSuchFieldException | IllegalAccessException ex){
				ex.printStackTrace();
			}
		});
		return dataSet;
	}

}