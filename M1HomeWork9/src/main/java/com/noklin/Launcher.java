package com.noklin;

import java.sql.SQLException;

import com.noklin.dao.DataSetDAO;
import com.noklin.entity.UserDataSet;

public class Launcher {

	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
		DataSetDAO<UserDataSet> userDAO = new DataSetDAO<>();
		UserDataSet user = new UserDataSet("Name", 123);
		user.setId(123);
		userDAO.save(user);
		System.out.println(userDAO.load(123, UserDataSet.class));
	}
	
} 