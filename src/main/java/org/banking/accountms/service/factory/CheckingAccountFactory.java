package org.banking.accountms.service.factory;

import org.banking.accountms.model.Account;
import org.banking.accountms.model.AccountType;

import java.math.BigDecimal;

public class CheckingAccountFactory implements AccountFactory {
    @Override
    public Account createAccount(Long clientId, BigDecimal initialBalance) {
        return Account.builder()
                .type(AccountType.CHECKING)
                .clientId(clientId)
                .balance(initialBalance)
                .active(true)
                .build();
    }
}