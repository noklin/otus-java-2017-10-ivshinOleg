package com.noklin.persist.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.noklin.persist.ConnectionFactory;
import com.noklin.persist.DataSet;

public class Executor {
	
	public <T extends DataSet> T execute(String query, List<TypedValue> params, ResultSetHandler<T> handler) throws SQLException{
		T result = null;
		try(Connection connection = ConnectionFactory.INSTANCE.getConnetion()){
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			setQueryParams(preparedStatement,params);
			try(ResultSet resultSet = preparedStatement.executeQuery()){
				setQueryParams(preparedStatement, params);
				result = handler.handle(resultSet);
			}finally{
				preparedStatement.close();
			}
		}
		return result;
	}

	public long executeUpdate(String query, List<TypedValue> fields) throws SQLException{
		long result = 0;
		try(Connection connection = ConnectionFactory.INSTANCE.getConnetion()){
			try(PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
				setQueryParams(preparedStatement,fields);
				result = preparedStatement.executeUpdate();
				try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
					if(generatedKeys.next()){
						result = generatedKeys.getLong("GENERATED_KEY");
					}
				}
			}
		}
		return result;
	}
	
	private void setQueryParams(PreparedStatement preparedStatement, List<TypedValue> values) throws SQLException{
		for(int i = 1 ; i <= values.size(); i++){
			TypedValue value = values.get(i - 1);
			if(value.getType() != TypedValue.Type.COLLECTION){
				setQueryParam(i, preparedStatement, value);
			}
		}
	}
	
	private void setQueryParam(int index, PreparedStatement preparedStatement, TypedValue value) throws SQLException{
		if(value.isNull()){
			preparedStatement.setNull(index, value.getType().getSqlType());
		}else{
			switch(value.getType()){
			case NUMBER:
				preparedStatement.setInt(index, value.asNumber().intValue());
				break;
			case STRING:
				preparedStatement.setString(index, value.asString());
				break;
			case RELATION:
				DataSet dataSet = value.asSomeType();
				preparedStatement.setLong(index, dataSet.getId());
				break;
			default:
				throw new RuntimeException("Unknown value type: " + value.getType());
			}
		}
	}
}
