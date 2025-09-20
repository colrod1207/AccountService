package org.banking.accountms.service.factory;

import java.math.BigDecimal;
import org.banking.accountms.model.Account;

public interface AccountFactory {
    Account createAccount(Long clientId, BigDecimal initialBalance);
}
