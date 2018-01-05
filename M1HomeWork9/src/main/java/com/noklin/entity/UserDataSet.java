package com.noklin.entity;

import javax.persistence.Table;
import com.noklin.persist.DataSet;

@Table(name = "user")
public class UserDataSet extends DataSet{

	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "UserDataSet [id=" + getId() + ", name=" + name + ", age=" + age + "]";
	}

	public UserDataSet(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public UserDataSet(){}
	
}