package org.banking.accountms.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    @Test
    void deposit_increasesBalance() {
        Account account = Account.builder().type(AccountType.SAVINGS).balance(BigDecimal.ZERO).clientId(1L).build();
        account.deposit(new BigDecimal("50"));
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("50"));
    }

    @Test
    void withdraw_savingsCannotGoNegative() {
        Account account = Account.builder()
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.TEN)
                .clientId(1L)
                .build();

        BigDecimal amount = new BigDecimal("20");

        assertThatThrownBy(() -> account.withdraw(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La cuenta de ahorros no puede quedar en saldo negativo.");
    }

}
