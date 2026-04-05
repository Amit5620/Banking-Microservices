package com.cognizant.bankingmgmt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.bankingmgmt.models.Account;
import com.cognizant.bankingmgmt.models.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsByAccountNumber(String accountNumber);

	Optional<Account> findByAccountNumber(String accountNumber);


}