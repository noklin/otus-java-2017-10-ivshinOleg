package com.noklin.persist.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.noklin.persist.DataSet;
import com.noklin.persist.PersistenceProperties;
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
	
	private void fillField(TypedValue field, ResultSet resultSet) throws SQLException{
		String fieldName = field.getName();
		switch(field.getType()){
		case NUMBER:
		case STRING:
			field.setValue(resultSet.getObject(fieldName));
			break;
		case RELATION:
			break;
		case COLLECTION:
			break;
		default: throw new RuntimeException("Unknown type: " + field.getType());	
		}
	}
	
	private T merge(EntityModel<T> model, ResultSet resultSet) throws SQLException{
		if(resultSet.next()){
			List<TypedValue> fields = model.getFields();
			for(TypedValue field : fields){
				fillField(field, resultSet);
			}
			T entity = model.constructEntity();
			return entity;
		}else{
			return null;
		}
	}
	
	private String generateSelectSql(EntityModel<T> model, List<WhereSqlCondition> whereConditions){
		StringBuilder builder = new StringBuilder("SELECT ");
		model.getFields().forEach(field -> {
			if(field.getType() != TypedValue.Type.COLLECTION){
				String fieldName = field.getFullName();
				builder.append(fieldName).append(",");
			}
		});
		builder.setLength(builder.length() - 1);
		builder.append(" FROM ").append(model.getTableName());
		if(!model.getJoinConditions().isEmpty()){
			appendJoin(builder, model.getJoinConditions());
		}
		if(!whereConditions.isEmpty()){
			appendCondition(builder, whereConditions);
		}
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
		beforePersistModel(model);
		long id = persistModel(model);
		dataSet.setId(id);
		afterPersistModel(dataSet, model);
	} 
	
	private long persistModel(EntityModel<T> model) throws SQLException{
		String query = generateInsertSql(model);
		String showSqlText = PersistenceProperties.PROPERTIES.get("potatoORM.show_sql");
		boolean showSql = Boolean.valueOf(showSqlText);
		if(showSql){
			System.out.println("Potato: " + query);
		}
		return executor.executeUpdate(query, model.getFields());
	}
	
	private void afterPersistModel(T dataSet, EntityModel<T> model) throws SQLException {
		for(TypedValue value : model.getFields()){
			switch(value.getType()){
			case NUMBER:
			case RELATION:
			case STRING:	
				break;
			case COLLECTION:
				Collection<T> entities = value.asCollection();
				for(T entity : entities){
					EntityModel<T> gg = new EntityModel<>(entity);
					@SuppressWarnings("unchecked")
					EntityModel<T> relationModel = EntityModel.createRelationModel(model.getTableName() +  "_" + gg.getTableName()
					, model.getRelationColumnName(), value.getName(), dataSet.getId(), entity.getId());
					persistModel(relationModel);
				}
				break;
			default : throw new RuntimeException("Unknown type: " + value.getType());	
			}
		}
	}

	private void beforePersistModel(EntityModel<T> model) throws SQLException{
		for(TypedValue value : model.getFields()){
			switch(value.getType()){
			case NUMBER:
			case STRING:	
				break;
			case COLLECTION:
				Collection<T> entities = value.asCollection();
				for(T entity : entities){
					persist(entity);
				}
				break;
			case RELATION:
				persist(value.asSomeType());
				break;
			default : throw new RuntimeException("Unknown type: " + value.getType());	
			}
		}
	}
	
	private String generateInsertSql(EntityModel<T> model){
		StringBuilder builder = new StringBuilder(" INSERT INTO ").append(model.getTableName()).append("(");
		model.getFields().forEach(field -> {
			if(field.getType() != TypedValue.Type.COLLECTION){
				String fieldName = field.getName();
				builder.append(fieldName).append(",");
			}
		});
		builder.setLength(builder.length() - 1);
		builder.append(") VALUES (");
		
		model.getFields().forEach(field -> {
			if(field.getType() != TypedValue.Type.COLLECTION){
				builder.append("?").append(",");
			}
		});
		builder.setLength(builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}
}