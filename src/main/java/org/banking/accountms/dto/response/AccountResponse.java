package org.banking.accountms.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banking.accountms.model.AccountType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType type;
    private Long clientId;
    private boolean active;
}
