package com.noklin.service;

import com.noklin.cache.Cache;
import com.noklin.persist.DataSet;

public class CachedDBService<T extends DataSet> implements DBService<T>{
	
	private final DBService<T> delegate;
	private final Cache<Long, T> cache;
	
	public CachedDBService(DBService<T> delegate, Cache<Long, T> cache){
		this.delegate = delegate; 
		this.cache = cache;
	}
	
	public void save(T entity) {
		delegate.save(entity);
		cache.put(entity.getId(), entity);
	}
	
	public T load(Class<T> entityClass, long id) {
		T result = cache.get(id);
		if(result == null){
			result = delegate.load(entityClass, id);
			if(result != null){
				cache.put(id, result);
			}
		}
		return result;
	}
	
	public void shutdown() {
		delegate.shutdown();
	}
	
}