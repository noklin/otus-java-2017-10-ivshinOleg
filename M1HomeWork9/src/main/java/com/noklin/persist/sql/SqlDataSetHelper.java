package com.noklin.persist.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.noklin.persist.DataSet;
import com.noklin.persist.sql.WhereSqlCondition.ConditionType;

public class SqlDataSetHelper<T extends DataSet> {
	
	private final Executor executor = new Executor(); 
	
	public T findById(long id, Class<T> entityClass) throws SQLException{
		T entity = null;
		checkEntityClass(entityClass);
		try{
			T dataSet = entityClass.newInstance();
			EntityModel<T> model = new EntityModel<>(dataSet);
			TypedValue typedValue = new TypedValue(model.getTableName(), DataSet.ID, long.class, id);
			String selectQuery = generateSelectSql(model, Arrays.asList(new WhereSqlCondition(ConditionType.EQUALS, typedValue.getFullName())));
			entity = executor.execute(selectQuery, Arrays.asList(typedValue), rs -> merge(model, rs));
		}catch(IllegalAccessException | InstantiationException ex){
			ex.printStackTrace();
		}
		return entity;
	}
	
	private void checkEntityClass(Class<T> entityClass){
		try{
			if(entityClass.getConstructor(new Class<?>[]{}) == null){
				throw new RuntimeException("Empty constructor is not found " + entityClass.getName());
			}
		}catch(NoSuchMethodException ex){
			ex.printStackTrace();
		}
	}
	
	private T merge(EntityModel<T> model, ResultSet resultSet) throws SQLException{
		if(resultSet.next()){
			List<TypedValue> fields = model.getFields();
			for(TypedValue field : fields){
				String fieldName = field.getName();
				field.setValue(resultSet.getObject(fieldName));
			}
			T entity = model.constructEntity();
			entity.setId(resultSet.getLong("id"));
			return entity;
		}else{
			return null;
		}
	}
	
	private String generateSelectSql(EntityModel<T> model, List<WhereSqlCondition> whereConditions){
		StringBuilder builder = new StringBuilder("SELECT ");
		model.getFields().forEach(field -> {
			String fieldName = field.getFullName();
			builder.append(fieldName).append(",");
		});
		builder.setLength(builder.length() - 1);
		builder.append(" FROM ").append(model.getTableName());
		if(!model.getJoinConditions().isEmpty()){
			appendJoin(builder, model.getJoinConditions());
		}
		if(!whereConditions.isEmpty()){
			appendCondition(builder, whereConditions);
		}
		System.out.println(" " + builder.toString());
		return builder.toString();
	}
	
	private void appendJoin(StringBuilder builder, List<JoinSqlCondition> joinConditions){
		builder.append(" JOIN ");
		for(int i = 0 ; i < joinConditions.size(); i++){
			JoinSqlCondition condition = joinConditions.get(i);
			builder.append(condition.getTableName()).append(" ON ");
			builder.append(condition.getLeftColumnName()).append(" = ").append(condition.getRightColumnName());
		}
	}
	
	private void appendCondition(StringBuilder builder, List<WhereSqlCondition> conditions){
		builder.append(" WHERE ");
		for(int i = 0 ; i < conditions.size(); i++){
			WhereSqlCondition condition = conditions.get(i);
			if(i > 0){
				builder.append(" AND ");
			}
			ConditionType type = condition.getConditionType();
			switch(type){
			case EQUALS:
				builder.append(condition.getName()).append(" = ").append("?");
				break;
			}
		}
	}
	
	public void persist(T dataSet) throws SQLException{
		EntityModel<T> model = new EntityModel<>(dataSet);
		String query = generateInsertSql(model);
		executor.executeUpdate(query, model.getFields());
	} 
	
	
	private String generateInsertSql(EntityModel<T> model){
		StringBuilder builder = new StringBuilder(" INSERT INTO ").append(model.getTableName()).append("(");
		model.getFields().forEach(field -> {
			String fieldName = field.getName();
			builder.append(fieldName).append(",");
		});
		builder.setLength(builder.length() - 1);
		builder.append(") VALUES (");
		
		model.getFields().forEach(field -> {
			builder.append("?").append(",");
		});
		builder.setLength(builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}
}
