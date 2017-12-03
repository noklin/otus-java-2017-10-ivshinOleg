package com.noklin.atm.item.money;

public class Rub extends Money{

	public Rub(int nominal) {
		super(nominal);
	}
	
	public Rub(){
		this(1);
	}

	@Override
	public String toString() {
		return getNominal() + " rub";
	}
}