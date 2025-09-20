package org.banking.accountms.mapper;

import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.model.Account;
import org.banking.accountms.model.AccountType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    @Test
    void testToResponse() {
        Account account = Account.builder()
                .id(1L)
                .accountNumber("CH-123456")
                .balance(BigDecimal.valueOf(500))
                .type(AccountType.CHECKING)
                .clientId(10L)
                .active(true)
                .build();

        AccountResponse response = AccountMapper.toResponse(account);

        assertEquals(1L, response.getId());
        assertEquals("CH-123456", response.getAccountNumber());
        assertEquals(BigDecimal.valueOf(500), response.getBalance());
        assertEquals(AccountType.CHECKING, response.getType());
        assertEquals(10L, response.getClientId());
        assertTrue(response.isActive());
    }

    @Test
    void testToEntity() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientId(20L);
        request.setType(AccountType.SAVINGS);
        request.setInitialBalance(BigDecimal.valueOf(1000));

        Account account = AccountMapper.toEntity(request);

        assertEquals(20L, account.getClientId());
        assertEquals(AccountType.SAVINGS, account.getType());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        Constructor<AccountMapper> constructor = AccountMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(ex.getCause() instanceof UnsupportedOperationException);
    }
}
