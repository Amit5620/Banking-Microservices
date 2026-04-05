package com.cognizant.bankingmgmt.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.cognizant.bankingmgmt.models.AccountType;


public record AccountResponse(Long id, String accountNumber, Long customerId, AccountType type, BigDecimal balance, OffsetDateTime createdAt) {

}

