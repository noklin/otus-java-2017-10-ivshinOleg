package com.noklin.atm.exception;

public class ATMServiceException extends Exception{
	private static final long serialVersionUID = 1L;
	public ATMServiceException(String msg){
		super(msg);
	}
}