package com.cognizant.bankingmgmt.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
	@NotBlank
	@Pattern(regexp = "^[A-Z0-9]{10,22}$", message = "Account number must be 10-22 uppercase alphanumeric")
	private String accountNumber;

	@NotNull
	@Positive
	private Long customerId;

	@NotBlank
	@Pattern(regexp = "Savings|Current|Checking|Loan", message = "type must be one of Savings|Current|Checking|Loan")
	private String type;

	@NotNull
	@DecimalMin(value = "0.00", message = "balance cannot be negative")
	private BigDecimal balance;
}