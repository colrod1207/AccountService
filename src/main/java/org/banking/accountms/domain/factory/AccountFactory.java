package org.banking.accountms.domain.factory;

import org.banking.accountms.domain.model.Account;

import java.math.BigDecimal;

public interface AccountFactory {
    Account createAccount(Long clientId, BigDecimal initialBalance);
}
