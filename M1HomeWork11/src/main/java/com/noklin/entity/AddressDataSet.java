package com.noklin.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.noklin.persist.DataSet;

@Entity
@DiscriminatorValue(value="A")
public class AddressDataSet extends DataSet{
	private String street;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public AddressDataSet(){}
	public AddressDataSet(String street){
		setStreet(street);
	}

	@Override
	public String toString() {
		return "AddressDataSet [street=" + street + "]";
	}
}