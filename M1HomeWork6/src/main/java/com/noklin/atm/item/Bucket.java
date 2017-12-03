package com.noklin.atm.item;

import java.util.ArrayList;

public class Bucket<T extends Item> extends ArrayList<T>{
	private static final long serialVersionUID = 1L;
	private final int nominal;
	public Bucket(int nominal){
		this.nominal = nominal;
	}
	
	public Bucket(Bucket<T> bucket){
		this(bucket.nominal);
		addAll(bucket);
	}
	
	public int getNominal() {
		return nominal;
	}
	
	@Override
	public String toString() {
		return "Bucket. Size: " + nominal + " Content: " + super.toString();
	}
}