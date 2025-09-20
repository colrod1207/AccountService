package org.banking.accountms.service.validation;

import java.math.BigDecimal;
import javax.validation.ValidationException;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.springframework.stereotype.Component;

@Component
public class BalanceValidation implements ValidationRule {
    @Override
    public void validate(CreateAccountRequest request) {
        if (request.getInitialBalance() == null ||
                request.getInitialBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El saldo inicial debe ser mayor que 0.");
        }
    }
}
