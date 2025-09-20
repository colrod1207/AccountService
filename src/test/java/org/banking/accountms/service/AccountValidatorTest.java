package org.banking.accountms.service;

import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.model.AccountType;
import org.banking.accountms.service.validation.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountValidatorTest {

    private ValidationRule mockRule1;
    private ValidationRule mockRule2;
    private AccountValidator validator;

    @BeforeEach
    void setUp() {
        mockRule1 = mock(ValidationRule.class);
        mockRule2 = mock(ValidationRule.class);
        validator = new AccountValidator(List.of(mockRule1, mockRule2));
    }

    private CreateAccountRequest buildRequest(Long clientId, BigDecimal balance, AccountType type) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientId(clientId);
        request.setInitialBalance(balance);
        request.setType(type);
        return request;
    }

    @Test
    void testValidateCallsAllRules() {
        CreateAccountRequest request = buildRequest(1L, BigDecimal.valueOf(100), AccountType.SAVINGS);

        validator.validate(request);

        verify(mockRule1, times(1)).validate(request);
        verify(mockRule2, times(1)).validate(request);
    }

    @Test
    void testValidateThrowsExceptionWhenRuleFails() {
        CreateAccountRequest request = buildRequest(2L, BigDecimal.ZERO, AccountType.SAVINGS);

        doThrow(new ValidationException("Regla inválida")).when(mockRule1).validate(request);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(request));
        assertEquals("Regla inválida", ex.getMessage());

        verify(mockRule1, times(1)).validate(request);
        verify(mockRule2, never()).validate(request);
    }

    @Test
    void testValidatePassesWhenNoRulesFail() {
        CreateAccountRequest request = buildRequest(3L, BigDecimal.valueOf(50), AccountType.CHECKING);

        assertDoesNotThrow(() -> validator.validate(request));

        verify(mockRule1, times(1)).validate(request);
        verify(mockRule2, times(1)).validate(request);
    }
}
