package com.cognizant.bankingmgmt.services;

import java.util.List;

import com.cognizant.bankingmgmt.models.AccountType;
import com.cognizant.bankingmgmt.models.Account;

public interface AccountService {
	Account addAccount(Account account);

	List<Account> getAllAccounts();

	Account getAccountById(Long id);

	Account getAccountByAccountNumber(String accountNumber);

	Account updateAccount(Long id, AccountType type);

	boolean deleteAccount(Long id);

}