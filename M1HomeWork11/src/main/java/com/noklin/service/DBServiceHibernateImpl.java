package com.noklin.service;

import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.noklin.persist.DataSet;

public class DBServiceHibernateImpl<T extends DataSet> implements DBService<T>{
	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
	private EntityManager entityManager = entityManagerFactory.createEntityManager(); 
	
	@Override
	public void save(T entity) {
		runInTransaction(() -> entityManager.persist(entity));
	}

	@Override
	public T load(Class<T> entityClass, long id) {
		Supplier<T> supplier = () ->  entityManager.find(entityClass, id);
		return runInTransaction(supplier);
	}
	
	private void runInTransaction(Runnable runable){
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		runable.run();
		tx.commit();
	}

	private	 T runInTransaction(Supplier<T> supplier){
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		try{
			return supplier.get();
		}finally{
			tx.commit();
		}
	}

	@Override
	public void shutdown() {
		entityManager.close();
		entityManagerFactory.close();
	}
}