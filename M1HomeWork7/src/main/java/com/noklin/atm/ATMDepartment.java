package com.noklin.atm;

import java.util.ArrayList;
import java.util.List;

import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.item.Item;

public class ATMDepartment<T extends Item> extends ArrayList<ATM<T>>{
	private static final long serialVersionUID = 1L;

	public long getTotalBalance(){
		long totalBalance= 0;
		for(ATM<T> atm : this){
			totalBalance += atm.getBalance();
		}
		return totalBalance;
	}
	
	public void backToCreationState() throws ATMServiceException{
		for(ATM<T> atm : this){
			atm.backToCreationState();
		}
	} 
	
	public ATMDepartment(){}
	public ATMDepartment(List<ATM<T>> list){
		super(list);
	}
}
