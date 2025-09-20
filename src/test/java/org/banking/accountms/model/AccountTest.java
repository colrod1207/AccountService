package org.banking.accountms.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    @Test
    void deposit_increasesBalance() {
        Account account = Account.builder()
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .clientId(1L)
                .build();

        account.deposit(new BigDecimal("50"));

        assertThat(account.getBalance())
                .isEqualByComparingTo(new BigDecimal("50"));
    }

    @Test
    void withdraw_savingsCannotGoNegative() {
        Account account = Account.builder()
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.TEN)
                .clientId(1L)
                .build();

        BigDecimal amount = new BigDecimal("20"); // ✅ fuera de la lambda

        assertThatThrownBy(() -> account.withdraw(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La cuenta de ahorros no puede quedar en saldo negativo.");
    }

    @Test
    void withdraw_checkingCanGoNegative() {
        Account account = Account.builder()
                .type(AccountType.CHECKING)
                .balance(BigDecimal.TEN)
                .clientId(1L)
                .build();

        account.withdraw(new BigDecimal("20"));

        assertThat(account.getBalance())
                .isEqualByComparingTo(new BigDecimal("-10"));
    }

    @Test
    void toString_shouldContainImportantFields() {
        Account account = Account.builder()
                .id(1L)
                .type(AccountType.SAVINGS)
                .balance(new BigDecimal("100"))
                .clientId(10L)
                .build();

        String result = account.toString();

        assertThat(result).contains("1", "SAVINGS", "100", "10");
    }

    @Test
    void equalsAndHashCode_shouldWorkProperly() {
        Account a1 = Account.builder()
                .id(1L)
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.TEN)
                .clientId(10L)
                .build();

        Account a2 = Account.builder()
                .id(1L)
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.TEN)
                .clientId(10L)
                .build();

        Account a3 = Account.builder()
                .id(2L)
                .type(AccountType.CHECKING)
                .balance(BigDecimal.ONE)
                .clientId(11L)
                .build();

        assertThat(a1)
                .isEqualTo(a2)
                .hasSameHashCodeAs(a2)
                .isNotEqualTo(a3);
    }

}
