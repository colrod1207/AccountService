package org.banking.accountms.infrastructure.repository;

import org.banking.accountms.domain.model.Account;
import org.banking.accountms.infrastructure.entity.AccountEntity;

public class AccountMapper {

    private AccountMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .type(account.getType())
                .clientId(account.getClientId())
                .active(account.isActive())
                .build();
    }

    public static Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getBalance(),
                entity.getType(),
                entity.getClientId(),
                entity.isActive()
        );
    }
}
