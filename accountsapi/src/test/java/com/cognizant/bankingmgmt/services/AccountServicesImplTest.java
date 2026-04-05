package com.cognizant.bankingmgmt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import com.cognizant.bankingmgmt.models.Account;
import com.cognizant.bankingmgmt.models.AccountType;
import com.cognizant.bankingmgmt.repositories.AccountRepository;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class AccountServicesImplTest {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountServiceImpl accountService;

	@Test
    public void addAccountMockTest() {
        Account account = new Account();
        account.setId(101L);
        account.setAccountNumber("ACC123456789");
        account.setCustomerId(500L);
        account.setType(AccountType.Checking);
        account.setBalance(new BigDecimal("1500.00"));
        account.setCreatedAt(java.time.OffsetDateTime.now());

//        doReturn(account).when(accountRepository).save(any(Account.class));
        
        Mockito.when(accountRepository.existsByAccountNumber("ACC123456789")).thenReturn(false);
        

//		RestClient restClient = null;
//		Mockito.lenient().when(restClient.get()).thenReturn(Mockito.mock(RestClient.RequestHeadersUriSpec.class, Mockito.RETURNS_DEEP_STUBS));

        Account response = accountService.addAccount(account);

        assertNotNull(response);
        assertEquals("ACC123456789", response.getAccountNumber());
        assertEquals(500L, response.getCustomerId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

	@Test
	public void getAllAccountsMockTest() {
		List<Account> accounts = getAllAccounts();
		Mockito.when(accountRepository.findAll()).thenReturn(accounts);

		List<Account> response = accountService.getAllAccounts();

		assertEquals(accounts.size(), response.size());
	}

	@Test
	public void getAccountByIdMockTest() {
		Account account = getAllAccounts().get(0);
		Long id = account.getId();

		Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));

		Account response = accountService.getAccountById(id);

		assertNotNull(response);
		assertEquals(id, response.getId());
	}

	@Test
	public void updateAccountMockTest() {
		Account existingAccount = getAllAccounts().get(0);
		Long id = existingAccount.getId();
		AccountType newType = AccountType.Savings; // Ensure this matches your Enum casing

		Mockito.when(accountRepository.existsById(id)).thenReturn(true);
		Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
		Mockito.when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

		Account response = accountService.updateAccount(id, newType);

		assertEquals(newType, response.getType());
		verify(accountRepository, times(1)).save(any(Account.class));
	}

	@Test
	public void deleteAccountMockTest() {
		Long id = 1L;
		Mockito.when(accountRepository.existsById(id)).thenReturn(true);

		boolean isDeleted = accountService.deleteAccount(id);

		assertTrue(isDeleted);
		verify(accountRepository, times(1)).deleteById(id);
	}

	private List<Account> getAllAccounts() {
		Faker faker = new Faker();
		List<Account> accounts = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Account account = new Account();
			account.setId(faker.number().randomNumber());
			account.setAccountNumber(faker.finance().iban());
			account.setCustomerId(faker.number().randomNumber());
			account.setBalance(BigDecimal.valueOf(faker.number().randomDouble(2, 500, 10000)));
			account.setType(AccountType.Checking);
			accounts.add(account);
		}
		return accounts;
	}
}