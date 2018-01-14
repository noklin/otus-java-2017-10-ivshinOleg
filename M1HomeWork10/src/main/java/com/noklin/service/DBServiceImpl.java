package com.noklin.service;

import java.sql.SQLException;

import com.noklin.dao.DataSetDAO;
import com.noklin.persist.DataSet;

public class DBServiceImpl<T extends DataSet> implements DBService<T>{
	private DataSetDAO<T> dataSetDAO = new DataSetDAO<>();
	
	@Override
	public void save(T entity) {
		try{
			dataSetDAO.save(entity);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public T load(Class<T> entityClass, long id) {
		T result = null;
		try{
			result = dataSetDAO.load(entityClass, id);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public void shutdown() {
		
	}

}