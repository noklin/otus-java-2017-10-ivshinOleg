package com.noklin.dao;

import java.sql.SQLException;

import com.noklin.persist.DataSet;
import com.noklin.persist.sql.SqlDataSetHelper;

public class DataSetDAO<T extends DataSet> {

	private SqlDataSetHelper<T> dbHelper = new SqlDataSetHelper<>();
	
	public T load(Class<T> entityClass, long id) throws SQLException{
		return dbHelper.findById(id, entityClass);
	}
	
	public void save(T entity) throws SQLException{
		dbHelper.persist(entity);
	}

}