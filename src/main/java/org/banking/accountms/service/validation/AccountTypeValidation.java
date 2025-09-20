package org.banking.accountms.service.validation;

import org.banking.accountms.dto.request.CreateAccountRequest;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;

@Component
public class AccountTypeValidation implements ValidationRule {
    @Override
    public void validate(CreateAccountRequest request) {
        if (request.getType() == null) {
            throw new ValidationException("El tipo de cuenta es obligatorio.");
        }
    }
}