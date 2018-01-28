package com.noklin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.noklin.cache.Cache;
import com.noklin.cache.CacheImpl;
import com.noklin.entity.AddressDataSet;
import com.noklin.entity.PhoneDataSet;
import com.noklin.entity.UserDataSet;
import com.noklin.service.CachedDBService;
import com.noklin.service.DBServiceHibernateImpl;

public enum Application {
	INSTANCE();
	{
		init();
	}
	
	private Cache<Long, UserDataSet> cache;
	private void init() {
		cache = CacheImpl.createBuilder().timeToLive(100).build();
		new Thread(this::startSaveSimulation).start();
		new Thread(this::startLoadSimulation).start();
	}

	public Cache<Long, UserDataSet>  getCahce(){
		return cache;
	}
	
	private static UserDataSet createUser(){
		UserDataSet user = new UserDataSet("Name", 123);
		PhoneDataSet phone1 = new PhoneDataSet("89003211421"); 
		PhoneDataSet phone2 = new PhoneDataSet("89184323454");
		PhoneDataSet phone3 = new PhoneDataSet("89184323452");
		Set<PhoneDataSet> phones = new HashSet<>(Arrays.asList(phone1,phone2,phone3));
		user.setAddress(new AddressDataSet("Solnechnaya"));
		user.setPhones(phones);
		return user;
	}
	
	private final Set<Long> ids = ConcurrentHashMap.newKeySet();
	private void startSaveSimulation(){
		CachedDBService<UserDataSet> pjaService = new CachedDBService<>(new DBServiceHibernateImpl<>(), cache);
		while(!Thread.currentThread().isInterrupted()){
			try{
				UserDataSet user = createUser();
				pjaService.save(user);
				ids.add(user.getId());
				TimeUnit.MILLISECONDS.sleep(250);
			}catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	private void startLoadSimulation(){
		CachedDBService<UserDataSet> pjaService = new CachedDBService<>(new DBServiceHibernateImpl<>(), cache);
		while(!Thread.currentThread().isInterrupted()){
			try{
				ids.forEach(id -> {
					pjaService.load(UserDataSet.class, id);
				});
				TimeUnit.MILLISECONDS.sleep(250);
			}catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}