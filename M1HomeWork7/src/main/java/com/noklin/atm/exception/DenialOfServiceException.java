package com.noklin.atm.exception;

public class DenialOfServiceException extends ATMServiceException{
	public DenialOfServiceException(String msg){
		super(msg);
	}
	private static final long serialVersionUID = 1L;
}
