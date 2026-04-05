
package com.cognizant.bankingmgmt.dtos;

import com.cognizant.bankingmgmt.dtos.GenericMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericMessage<T> {
	private T object;
	private String message;
	
	public GenericMessage(T object) {
		super();
		this.object = object;
	}
	
	public GenericMessage(String message) {
		super();
		this.message = message;
	}
}

