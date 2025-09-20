package org.banking.accountms.service.validation;

import org.banking.accountms.dto.request.CreateAccountRequest;

public interface ValidationRule {
    void validate(CreateAccountRequest request);
}
