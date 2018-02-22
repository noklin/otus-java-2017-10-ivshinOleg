package com.noklin.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.noklin.persist.DataSet;

@Entity
@DiscriminatorValue(value="P")
public class PhoneDataSet extends DataSet{
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public PhoneDataSet(){}
	public PhoneDataSet(String number){
		setNumber(number);
	}

	@Override
	public String toString() {
		return "PhoneDataSet [number=" + number + "]";
	}
	
}