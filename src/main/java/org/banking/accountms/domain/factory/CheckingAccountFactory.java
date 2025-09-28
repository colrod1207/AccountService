package org.banking.accountms.domain.factory;

import org.banking.accountms.domain.model.Account;
import org.banking.accountms.domain.model.AccountType;

import java.math.BigDecimal;

public class CheckingAccountFactory implements AccountFactory {

    @Override
    public Account createAccount(Long clientId, BigDecimal initialBalance) {
        return new Account(
                null,
                null,
                initialBalance,
                AccountType.CHECKING,
                clientId,
                true
        );
    }
}

