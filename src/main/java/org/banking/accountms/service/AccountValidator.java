package org.banking.accountms.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.service.validation.ValidationRule;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    private final List<ValidationRule> rules;

    public void validate(CreateAccountRequest request) {
        rules.forEach(rule -> rule.validate(request));
    }
}
