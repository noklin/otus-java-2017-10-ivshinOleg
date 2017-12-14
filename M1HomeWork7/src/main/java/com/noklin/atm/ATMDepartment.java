package com.noklin.atm;

import java.util.Collection;

import com.noklin.atm.exception.ATMServiceException;

public class ATMDepartment{ 

	public long getTotalBalance(){
		long totalBalance= 0;
		for(ATM<?> atm : atms){
			totalBalance += atm.getBalance();
		}
		return totalBalance;
	}
	
	public void backToCreationState() throws ATMServiceException{
		for(ATM<?> atm : atms){
			atm.backToCreationState();
		}
	} 
	
	public ATMDepartment(){}
	public ATMDepartment(Collection<ATM<?>> atms){
		this.atms = atms;
	}
	
	private Collection<ATM<?>> atms;
}
