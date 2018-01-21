package com.noklin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.noklin.cache.Cache;
import com.noklin.cache.CacheImpl;
import com.noklin.entity.AddressDataSet;
import com.noklin.entity.PhoneDataSet;
import com.noklin.entity.UserDataSet;
import com.noklin.service.CachedDBService;
import com.noklin.service.DBServiceHibernateImpl;
import com.noklin.service.DBServiceImpl; 

public class Launcher{
	
	
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
	
	public static void main(String[] args) throws SQLException, InterruptedException{
		UserDataSet user = createUser();
		Cache<Long, UserDataSet> cache = CacheImpl.createBuilder().timeToLive(100).build();
		CachedDBService<UserDataSet> defService = new CachedDBService<>(new DBServiceImpl<>(), cache);
		CachedDBService<UserDataSet> pjaService = new CachedDBService<>(new DBServiceHibernateImpl<>(), cache);
		defService.save(user);
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		TimeUnit.MILLISECONDS.sleep(150);
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		TimeUnit.MILLISECONDS.sleep(150);
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		TimeUnit.MILLISECONDS.sleep(150);
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		System.out.println("HitCount: " + cache.getHitCount());
		System.out.println("MissCount: " +cache.getMissCount());
		pjaService.shutdown();
	}

} 