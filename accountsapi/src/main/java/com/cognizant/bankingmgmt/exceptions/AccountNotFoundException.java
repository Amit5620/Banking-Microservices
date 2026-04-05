package com.cognizant.bankingmgmt.exceptions;

import java.io.Serializable;

public class AccountNotFoundException extends RuntimeException implements Serializable{
	
	public AccountNotFoundException(String message) {
		super(message);
	}

}
