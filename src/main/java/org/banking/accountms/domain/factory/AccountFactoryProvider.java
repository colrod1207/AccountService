package org.banking.accountms.domain.factory;

import org.banking.accountms.domain.model.AccountType;

public class AccountFactoryProvider {

    private AccountFactoryProvider() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static AccountFactory getFactory(AccountType type) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccountFactory();
            case CHECKING:
                return new CheckingAccountFactory();
            default:
                throw new IllegalArgumentException("Tipo de cuenta no soportado: " + type);
        }
    }
}
