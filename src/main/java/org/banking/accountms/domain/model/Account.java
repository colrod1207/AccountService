package org.banking.accountms.domain.model;

import org.banking.accountms.domain.exception.Messages;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType type;
    private Long clientId;
    private boolean active;

    public Account(Long id, String accountNumber, BigDecimal balance, AccountType type, Long clientId, boolean active) {
        this.id = id;
        // ✅ permitir que sea null al inicio, se asigna luego con assignAccountNumber
        this.accountNumber = accountNumber;
        this.balance = Objects.requireNonNullElse(balance, BigDecimal.ZERO);
        this.type = Objects.requireNonNull(type, "type is required");
        this.clientId = Objects.requireNonNull(clientId, "clientId is required");
        this.active = active;
    }

    // Constructor sin ID (cuando recién se crea)
    public Account(String accountNumber, BigDecimal balance, AccountType type, Long clientId) {
        this(null, accountNumber, balance, type, clientId, true);
    }

    // Getters (sin setters → inmutabilidad controlada)
    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public AccountType getType() { return type; }
    public Long getClientId() { return clientId; }
    public boolean isActive() { return active; }

    // === Reglas de negocio ===

    /** Regla crítica: no permitir depósitos no positivos */
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo.");
        }
        this.balance = this.balance.add(amount);
    }

    /** Regla crítica: validar retiros según el tipo de cuenta */
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de retiro debe ser mayor que cero.");
        }

        BigDecimal next = this.balance.subtract(amount);

        if (type == AccountType.SAVINGS && next.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cuenta de ahorros no puede quedar en saldo negativo.");
        }
        if (type == AccountType.CHECKING && next.compareTo(new BigDecimal("-500")) < 0) {
            throw new IllegalArgumentException("La cuenta corriente no puede tener saldo menor a -500.");
        }

        this.balance = next;
    }

    public void activate() {
        if (active) {
            throw new ValidationException(Messages.ACCOUNT_ALREADY_ACTIVE);
        }
        this.active = true;
    }

    public void deactivate() {
        if (!active) {
            throw new ValidationException(Messages.ACCOUNT_ALREADY_INACTIVE);
        }
        this.active = false;
    }

    /** Solo permite asignar número de cuenta una vez */
    public void assignAccountNumber(String accountNumber) {
        if (this.accountNumber != null) {
            throw new IllegalStateException("El número de cuenta ya fue asignado");
        }
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber es requerido");
    }
}
