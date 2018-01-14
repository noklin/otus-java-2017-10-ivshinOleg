package com.noklin.persist.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import com.noklin.persist.DataSet;

class EntityModel<T extends DataSet> {
	
	private String tableName;
	private String relationColumnName;
	private String discriminatorValue;
	private String discriminatorColumnName;
	private Class<?> discriminatorType;
		
	private List<TypedValue> fields = new ArrayList<>(); 
	private T dataSet;
	
	EntityModel(T dataSet){
		this.dataSet = dataSet;
		recognizeTableName();
		recognizeDiscriminator();
		recognizeDiscriminatorValue();
		recognizeColumns();
	}

	T unWrap(){
		return (T)dataSet;
	}
	
	private void recognizeColumns() {
		try{
			long id = dataSet.getId();
			if(DataSet.EMPTY_ID != id){
				fields.add(new TypedValue(tableName, DataSet.ID, DataSet.ID_TYPE, id));
			}
			fields.add(new TypedValue(tableName, discriminatorColumnName, discriminatorType, discriminatorValue));
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
		Class<?> superClass = entityClass.getSuperclass();
		tableName = superClass.getAnnotation(Table.class).name();
		relationColumnName = entityClass.getSimpleName();
	}
	
	public String getRelationColumnName() {
		return relationColumnName;
	}

	private void recognizeDiscriminatorValue() {
		Class<?> entityClass = dataSet.getClass();
		if(entityClass.isAnnotationPresent(DiscriminatorValue.class)){
			discriminatorValue = entityClass.getAnnotation(DiscriminatorValue.class).value();
		}else{
			discriminatorValue = dataSet.getClass().getSimpleName().toLowerCase(); 
		}
	}

	private void recognizeDiscriminator() {
		Class<?> entityClass = dataSet.getClass();
		Class<?> superClass = entityClass.getSuperclass();
		DiscriminatorColumn discriminator = superClass.getAnnotation(DiscriminatorColumn.class);
		DiscriminatorType type = discriminator.discriminatorType();
		switch(type){
		case STRING:
			discriminatorType = String.class;
			break;
		case CHAR:
			discriminatorType = Character.class;
			break;
		case INTEGER:
			discriminatorType = Integer.class;
			break;
		}
		discriminatorColumnName = discriminator.name();
	}
	
	String getDiscriminatorValue() {
		return discriminatorValue;
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
	
	private boolean isLegal(TypedValue value){
		if(DataSet.ID.equals(value.getName())){
			return false;
		}else if(value.getType() == TypedValue.Type.COLLECTION) {
			return false;
		}else if(value.getType() == TypedValue.Type.RELATION){
			return false;
		}else if(discriminatorColumnName.equals(value.getName())){
			return false;
		}
		return true;
	}
	
	T constructEntity(){
		fields.forEach(val -> {
			try{
				if(isLegal(val)){
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
	
	private EntityModel(){}
	
	@SuppressWarnings("rawtypes")
	public static <T> EntityModel createRelationModel(String relationTableName, String leftColumnName
			, String rightColumnName, long leftId, long rightId){
		return new EntityModel(){
			@Override
			String getTableName() {
				return relationTableName;
			}
			@Override
			List<TypedValue> getFields() {
				TypedValue leftIdValue = new TypedValue(relationTableName, leftColumnName + "_id", DataSet.ID_TYPE, leftId);
				TypedValue rightIdValue  = new TypedValue(relationTableName, rightColumnName + "_id", DataSet.ID_TYPE, rightId);
				return Arrays.asList(leftIdValue, rightIdValue);
			}
		};
	}
}