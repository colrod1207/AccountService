package org.banking.accountms.application.service;

import lombok.RequiredArgsConstructor;
import org.banking.accountms.application.validation.ValidationRule;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    private final List<ValidationRule> rules;

    public void validate(CreateAccountRequest request) {
        rules.forEach(rule -> rule.validate(request));
    }
}
