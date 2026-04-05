package com.cognizant.bankingmgmt.services;

import com.cognizant.bankingmgmt.exceptions.AccountNotFoundException;
import com.cognizant.bankingmgmt.exceptions.AccountNullException;
import com.cognizant.bankingmgmt.models.Account;
import com.cognizant.bankingmgmt.models.AccountType;
import com.cognizant.bankingmgmt.repositories.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RestClient restClient;
	@Value("${gatewayUrl}")
	private String gatewayUrl;
	@Value("${serviceUrl}")
	private String serviceUrl;

	@Override
	public Account addAccount(Account account) {
		if (account == null) {
			throw new AccountNullException("Account object is null");
		}
		if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
			throw new AccountNullException("Account number already exists");
		}
		if (account.getBalance() != null && account.getBalance().signum() < 0) {
			throw new AccountNullException("Balance cannot be negative");
		}

		String token = restClient.get().uri(gatewayUrl).retrieve().body(String.class);

		boolean exists = restClient.get()
	            .uri(serviceUrl + "/{id}", account.getCustomerId())
	            .headers(h -> h.setBearerAuth(token))
	            .retrieve()
	            .onStatus(status -> status.value() == 404, (req, res) -> { }) 
	            .toBodilessEntity()
	            .getStatusCode()
	            .is2xxSuccessful();

		if (!exists)
			throw new AccountNotFoundException("Customer not found with id: " + account.getCustomerId());
		
		
		return accountRepository.save(account);
	}

	@Override
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public Account getAccountById(Long id) {
		return accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));
	}

	@Override
	public Account getAccountByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
	}

	@Override
	public Account updateAccount(Long id, AccountType type) {
		if (type == null) {
			throw new AccountNullException("Account type cannot be null");
		}
		if (!accountRepository.existsById(id)) {
			throw new AccountNotFoundException("Account not found to update");
		}
		Account acc = accountRepository.findById(id).get();

		acc.setType(type);
		return accountRepository.save(acc);
	}

	@Override
	public boolean deleteAccount(Long id) {
		if (accountRepository.existsById(id)) {
			accountRepository.deleteById(id);
			return true;
		}
		return false;
	}

}