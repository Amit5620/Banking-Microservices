package com.cognizant.bankingmgmt.exceptions;

import java.io.Serializable;

public class AccountNullException extends RuntimeException implements Serializable{
	
	public AccountNullException(String message) {
		super(message);
	}

}
