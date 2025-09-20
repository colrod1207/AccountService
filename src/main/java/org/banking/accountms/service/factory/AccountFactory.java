package org.banking.accountms.service.factory;

import org.banking.accountms.model.Account;

import java.math.BigDecimal;

public interface AccountFactory {
    Account createAccount(Long clientId, BigDecimal initialBalance);
}
