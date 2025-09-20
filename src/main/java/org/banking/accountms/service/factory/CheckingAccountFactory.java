package org.banking.accountms.service.factory;

import java.math.BigDecimal;
import org.banking.accountms.model.Account;
import org.banking.accountms.model.AccountType;

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
