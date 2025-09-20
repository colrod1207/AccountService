package org.banking.accountms.mapper;

import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.model.Account;

public class AccountMapper {

    private AccountMapper() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Convierte una entidad Account a un DTO AccountResponse.
     */
    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getType(),
                account.getClientId(),
                account.isActive()
        );
    }

    /**
     * Convierte un DTO CreateAccountRequest a una entidad Account.
     */
    public static Account toEntity(CreateAccountRequest request) {
        return Account.builder()
                .clientId(request.getClientId())
                .type(request.getType())
                .balance(request.getInitialBalance())
                .build();
    }
}
