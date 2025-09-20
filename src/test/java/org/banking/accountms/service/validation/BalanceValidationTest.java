package org.banking.accountms.service.validation;

import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.model.AccountType;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class BalanceValidationTest {

    private final BalanceValidation validation = new BalanceValidation();

    @Test
    void validate_withValidBalance_doesNotThrow() {
        CreateAccountRequest request = new CreateAccountRequest(
                1L, AccountType.SAVINGS, new BigDecimal("100")
        );

        assertThatCode(() -> validation.validate(request))
                .doesNotThrowAnyException();
    }

    @Test
    void validate_withZeroBalance_throwsValidationException() {
        CreateAccountRequest request = new CreateAccountRequest(
                1L, AccountType.SAVINGS, BigDecimal.ZERO
        );

        assertThatThrownBy(() -> validation.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("El saldo inicial debe ser mayor que 0.");
    }

    @Test
    void validate_withNegativeBalance_throwsValidationException() {
        CreateAccountRequest request = new CreateAccountRequest(
                1L, AccountType.SAVINGS, new BigDecimal("-10")
        );

        assertThatThrownBy(() -> validation.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("El saldo inicial debe ser mayor que 0.");
    }


}
