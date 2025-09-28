package org.banking.accountms.domain.factory;

import org.banking.accountms.domain.model.Account;
import org.banking.accountms.domain.model.AccountType;

import java.math.BigDecimal;

public class SavingsAccountFactory implements AccountFactory {

    @Override
    public Account createAccount(Long clientId, BigDecimal initialBalance) {
        return new Account(
                null,             // id → generado en persistencia
                null,             // accountNumber → se asigna después con el generador
                initialBalance,
                AccountType.SAVINGS,
                clientId,
                true
        );
    }
}

