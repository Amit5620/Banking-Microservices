package com.cognizant.bankingmgmt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cognizant.bankingmgmt.dtos.AccountDTO;
import com.cognizant.bankingmgmt.dtos.AccountResponse;
import com.cognizant.bankingmgmt.models.AccountType;
import com.cognizant.bankingmgmt.dtos.GenericMessage;
import com.cognizant.bankingmgmt.mappers.AccountMapper;
import com.cognizant.bankingmgmt.models.Account;
import com.cognizant.bankingmgmt.services.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountMapper accountMapper;

	@PreAuthorize("hasAnyAuthority('SCOPE_developer')")
	@PostMapping("/v1.0")
	public ResponseEntity<GenericMessage> addAccount(@Valid @RequestBody AccountDTO dto) {

		Account toSave = accountMapper.toEntity(dto);
		Account saved = accountService.addAccount(toSave);
		AccountResponse response = accountMapper.toDTO(saved);

		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericMessage(response));
	}

	@PreAuthorize("hasAnyAuthority('SCOPE_developer', 'SCOPE_tester')")
	@GetMapping("/v1.0")
	public List<AccountResponse> getAllAccounts() {
		return accountMapper.toDTOs(accountService.getAllAccounts());
	}

	@PreAuthorize("hasAnyAuthority('SCOPE_developer', 'SCOPE_tester')")
	@GetMapping("/v1.0/{id}")
	public ResponseEntity<GenericMessage> getAccountById(@PathVariable("id") Long id) {
		Account account = accountService.getAccountById(id);
		AccountResponse response = accountMapper.toDTO(account);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new GenericMessage(response));
	}

	@PreAuthorize("hasAnyAuthority('SCOPE_developer', 'SCOPE_tester')")
	@GetMapping(value = "/v1.0/search", params = "accountNumber")
	public ResponseEntity<GenericMessage> getAccountByAccountNumber(@RequestParam String accountNumber) {
		Account account = accountService.getAccountByAccountNumber(accountNumber);
		return ResponseEntity.ok(new GenericMessage(accountMapper.toDTO(account)));
	}

	@PreAuthorize("hasAnyAuthority('SCOPE_developer')")
	@PatchMapping("/v1.0")
	public ResponseEntity<GenericMessage> updateAccountType(@RequestParam Long id,
			@RequestParam AccountType type) {

		Account updated = accountService.updateAccount(id, type);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new GenericMessage(accountMapper.toDTO(updated)));
	}

	@PreAuthorize("hasAnyAuthority('SCOPE_developer')")
	@DeleteMapping("/v1.0")
	public ResponseEntity<GenericMessage> deleteAccount(@RequestParam Long id) {
		boolean deleted = accountService.deleteAccount(id);
		if (deleted) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new GenericMessage(id));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericMessage(id));
		}
	}
}