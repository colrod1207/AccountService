package org.banking.accountms.service.factory;

import org.banking.accountms.model.AccountType;

public class AccountFactoryProvider {

    // Oculta el constructor para que nadie pueda hacer "new AccountFactoryProvider()"
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
