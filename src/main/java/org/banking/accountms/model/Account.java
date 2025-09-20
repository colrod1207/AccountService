package org.banking.accountms.model;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private boolean active = true;

    /**
     Regla crítica: no permitir depósitos no positivos.
     **/
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    /**
     * Regla crítica: no permitir retiros que violen el tipo de cuenta.
     */
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del retiro debe ser mayor que cero.");
        }

        BigDecimal next = this.balance.subtract(amount);

        if (type == AccountType.SAVINGS && next.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cuenta de ahorros no puede quedar en saldo negativo.");
        }
        if (type == AccountType.CHECKING && next.compareTo(new BigDecimal("-500")) < 0) {
            throw new IllegalArgumentException("La cuenta corriente no puede tener un saldo menor a -500.");
        }

        this.balance = next;
    }
}
