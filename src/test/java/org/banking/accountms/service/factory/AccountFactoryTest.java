package org.banking.accountms.service.factory;

import org.banking.accountms.model.Account;
import org.banking.accountms.model.AccountType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountFactoryTest {

    @Test
    void testCheckingAccountFactoryCreatesAccount() {
        CheckingAccountFactory factory = new CheckingAccountFactory();
        Account acc = factory.createAccount(1L, BigDecimal.valueOf(500));

        assertEquals(AccountType.CHECKING, acc.getType());
        assertEquals(1L, acc.getClientId());
        assertEquals(BigDecimal.valueOf(500), acc.getBalance());
        assertTrue(acc.isActive());
    }

    @Test
    void testSavingsAccountFactoryCreatesAccount() {
        SavingsAccountFactory factory = new SavingsAccountFactory();
        Account acc = factory.createAccount(2L, BigDecimal.valueOf(1000));

        assertEquals(AccountType.SAVINGS, acc.getType());
        assertEquals(2L, acc.getClientId());
        assertEquals(BigDecimal.valueOf(1000), acc.getBalance());
        assertTrue(acc.isActive());
    }

    @Test
    void testAccountFactoryProviderReturnsCorrectFactory() {
        assertTrue(AccountFactoryProvider.getFactory(AccountType.SAVINGS) instanceof SavingsAccountFactory);
        assertTrue(AccountFactoryProvider.getFactory(AccountType.CHECKING) instanceof CheckingAccountFactory);
    }

    @Test
    void testAccountFactoryProviderThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> AccountFactoryProvider.getFactory(null));
    }

    @Test
    void testAccountFactoryProviderPrivateConstructor() throws Exception {
        var constructor = AccountFactoryProvider.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        assertTrue(ex.getCause() instanceof UnsupportedOperationException);
        assertEquals("Utility class", ex.getCause().getMessage());
    }

}
