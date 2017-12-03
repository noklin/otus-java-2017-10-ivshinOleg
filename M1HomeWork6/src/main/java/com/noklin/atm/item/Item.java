package com.noklin.atm.item;

public class Item { 
	private int nominal;
	public Item(int nominal){
		this.nominal = nominal;
	}
	
	public int getNominal() {
		return nominal;
	}

	public void setNominal(int nominal) {
		this.nominal = nominal;
	}
	
}