package com.noklin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.noklin.entity.AddressDataSet;
import com.noklin.entity.PhoneDataSet;
import com.noklin.entity.UserDataSet;
import com.noklin.service.DBService;
import com.noklin.service.DBServiceHibernateImpl;
import com.noklin.service.DBServiceImpl; 

public class Launcher {
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
		UserDataSet user = new UserDataSet("Name", 123);
		PhoneDataSet phone1 = new PhoneDataSet("89003211421"); 
		PhoneDataSet phone2 = new PhoneDataSet("89184323454");
		PhoneDataSet phone3 = new PhoneDataSet("89184323452");
		Set<PhoneDataSet> phones = new HashSet<>(Arrays.asList(phone1,phone2,phone3));
		user.setAddress(new AddressDataSet("Solnechnaya"));
		user.setPhones(phones);
		DBService<UserDataSet> defService = new DBServiceImpl<>();
		DBService<UserDataSet> pjaService = new DBServiceHibernateImpl<>();
		defService.save(user);
		System.out.println(pjaService.load(UserDataSet.class, user.getId()));
		pjaService.shutdown();
	}
} 