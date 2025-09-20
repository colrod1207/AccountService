package org.banking.accountms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banking.accountms.model.AccountType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NotNull(message = "El clientId es obligatorio.")
    private Long clientId;
    @NotNull(message = "El tipo de cuenta es obligatorio.")
    private AccountType type;
    @Positive(message = "El balance inicial debe ser mayor a 0.")
    private BigDecimal initialBalance;
}
