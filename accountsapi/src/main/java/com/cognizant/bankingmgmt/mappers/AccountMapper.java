
package com.cognizant.bankingmgmt.mappers;

import org.mapstruct.Mapper;

import com.cognizant.bankingmgmt.dtos.AccountDTO;
import com.cognizant.bankingmgmt.dtos.AccountResponse;
import com.cognizant.bankingmgmt.models.Account;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	Account toEntity(AccountDTO dto);

	AccountResponse toDTO(Account entity);

	List<AccountResponse> toDTOs(List<Account> entities);
}