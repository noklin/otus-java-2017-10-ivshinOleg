package com.noklin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.noklin.entity.AddressDataSet;
import com.noklin.entity.PhoneDataSet;
import com.noklin.entity.UserDataSet;
import com.noklin.service.DBService;

@Component
public class App {
	
	@Autowired
	@Qualifier("cached")
	private DBService<UserDataSet> dbServiceForSave;

	@Autowired
	@Qualifier("cached")
	private DBService<UserDataSet> dbServiceForLoad;
	
	@PostConstruct
	public void init() {
		Thread saver = new Thread(this::startSaveSimulation);
		saver.setDaemon(true);
		saver.start();
		Thread loader = new Thread(this::startLoadSimulation);
		loader.setDaemon(true);
		loader.start();
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
		while(!Thread.currentThread().isInterrupted()){
			try{
				UserDataSet user = createUser();
				dbServiceForSave.save(user);
				ids.add(user.getId());
				TimeUnit.MILLISECONDS.sleep(250);
			}catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void startLoadSimulation(){
		while(!Thread.currentThread().isInterrupted()){
			try{
				ids.forEach(id -> {
					dbServiceForLoad.load(UserDataSet.class, id);
				});
				TimeUnit.MILLISECONDS.sleep(250);
			}catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
