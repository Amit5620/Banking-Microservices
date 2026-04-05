package com.cognizant.bankingmgmt.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.bankingmgmt.dtos.AccountDTO;
import com.cognizant.bankingmgmt.dtos.AccountResponse;
import com.cognizant.bankingmgmt.mappers.AccountMapper;
import com.cognizant.bankingmgmt.models.Account;
import com.cognizant.bankingmgmt.models.AccountType;
import com.cognizant.bankingmgmt.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountMapper accountMapper;

    @Test
    public void testAddAccount() throws Exception {
        AccountDTO dto = new AccountDTO();
        dto.setAccountNumber("ABC1234567890");
        dto.setCustomerId(1L);
        dto.setType("Savings");
        dto.setBalance(new java.math.BigDecimal("1000.00"));

        Account account = getMockAccounts(1).get(0);

        Mockito.when(accountMapper.toEntity(Mockito.any(AccountDTO.class))).thenReturn(account);
        Mockito.when(accountService.addAccount(Mockito.any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/accounts/v1.0")
                .with(jwt().authorities(() -> "SCOPE_developer"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        List<Account> accounts = getMockAccounts(5);
        List<AccountResponse> responses = new ArrayList<>();

        Mockito.when(accountService.getAllAccounts()).thenReturn(accounts);
        Mockito.when(accountMapper.toDTOs(accounts)).thenReturn(responses);

        mockMvc.perform(get("/accounts/v1.0")
                .with(jwt().authorities(() -> "SCOPE_tester")))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetAccountById() throws Exception {
        Long id = 1L;
        Account account = getMockAccounts(1).get(0);

        Mockito.when(accountService.getAccountById(id)).thenReturn(account);

        mockMvc.perform(get("/accounts/v1.0/{id}", id)
                .with(jwt().authorities(() -> "SCOPE_tester")))
            .andExpect(status().isAccepted());
    }

    @Test
    public void testGetAccountByAccountNumber() throws Exception {
        String accNum = "ACC12345";
        Account account = getMockAccounts(1).get(0);
        
        Mockito.when(accountService.getAccountByAccountNumber(accNum)).thenReturn(account);

        mockMvc.perform(get("/accounts/v1.0/search")
                .param("accountNumber", accNum)
                .with(jwt().authorities(() -> "SCOPE_developer")))
            .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAccountType() throws Exception {
        Long id = 1L;
        AccountType type = AccountType.Savings;
        Account updatedAccount = getMockAccounts(1).get(0);

        Mockito.when(accountService.updateAccount(id, type)).thenReturn(updatedAccount);

        mockMvc.perform(patch("/accounts/v1.0")
                .param("id", id.toString())
                .param("type", type.name())
                .with(jwt().authorities(() -> "SCOPE_developer")))
            .andExpect(status().isAccepted());
    }

    @Test
    public void testDeleteAccount_Success() throws Exception {
        Long id = 1L;
        Mockito.when(accountService.deleteAccount(id)).thenReturn(true);

        mockMvc.perform(delete("/accounts/v1.0")
                .param("id", id.toString())
                .with(jwt().authorities(() -> "SCOPE_developer")))
            .andExpect(status().isAccepted());
    }

    @Test
    public void testDeleteAccount_NotFound() throws Exception {
        Long id = 99L;
        Mockito.when(accountService.deleteAccount(id)).thenReturn(false);

        mockMvc.perform(delete("/accounts/v1.0")
                .param("id", id.toString())
                .with(jwt().authorities(() -> "SCOPE_developer")))
            .andExpect(status().isNotFound());
    }

    private List<Account> getMockAccounts(int count) {
        Faker faker = new Faker();
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Account account = new Account();
            account.setId(faker.number().randomNumber());
            account.setAccountNumber(faker.finance().iban());
            accounts.add(account);
        }
        return accounts;
    }
}