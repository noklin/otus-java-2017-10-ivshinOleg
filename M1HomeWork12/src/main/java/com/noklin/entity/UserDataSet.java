package com.noklin.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.noklin.persist.DataSet;
 
@Entity
@DiscriminatorValue(value="U")
public class UserDataSet extends DataSet{

	private String name;
	private int age;
	
	@OneToOne(cascade = CascadeType.ALL)
	private AddressDataSet address;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<PhoneDataSet> phones;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public AddressDataSet getAddress() {
		return address;
	}
	public void setAddress(AddressDataSet address) {
		this.address = address;
	}
	public Set<PhoneDataSet> getPhones() {
		return phones;
	}
	public void setPhones(Set<PhoneDataSet> phones) {
		this.phones = phones;
	}
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public UserDataSet(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public UserDataSet(){}
	@Override
	public String toString() {
		return "UserDataSet [name=" + name + ", age=" + age + ", address=" + address + ", phones=" + phones + "]";
	}
}