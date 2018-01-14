package com.noklin.service;

import com.noklin.persist.DataSet;

public interface DBService<T extends DataSet>{
	void save(T entity);
	T load(Class<T> entityClass, long id);
	void shutdown();
}