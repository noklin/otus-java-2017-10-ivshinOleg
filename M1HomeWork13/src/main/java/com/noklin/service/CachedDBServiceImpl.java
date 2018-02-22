package com.noklin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.noklin.cache.Cache;
import com.noklin.persist.DataSet;

@Service("cached")
@Scope("prototype")
public class CachedDBServiceImpl<T extends DataSet> implements DBService<T>{
	
	private final DBService<T> delegate;
	private final Cache<Long, T> cache;
	
	public CachedDBServiceImpl(@Autowired @Qualifier("hibernate") DBService<T> delegate, @Autowired Cache<Long, T> cache){
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