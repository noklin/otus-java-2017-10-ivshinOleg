package com.noklin.atm.operation;

import com.noklin.atm.ATM;
import com.noklin.atm.ATM.Memento;
import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.item.Item;

public abstract class Operation<T extends Item> {
	
	private final ATM<T> atm;
	private final Memento<T> beforeOperationState;
	protected Operation(ATM<T> atm){
		this.atm = atm;
		beforeOperationState = atm.getState();
	}
	
	public ATM<T> getAtm() {
		return atm;
	}
	
	public abstract void doOperaion() throws ATMServiceException;
	public void undoOperaion() throws ATMServiceException{
		atm.setState(beforeOperationState);
	}
}