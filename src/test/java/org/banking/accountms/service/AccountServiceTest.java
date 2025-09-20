package org.banking.accountms.service;

import org.banking.accountms.adapter.ClientGateway;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.exception.ResourceNotFoundException;
import org.banking.accountms.model.Account;
import org.banking.accountms.model.AccountType;
import org.banking.accountms.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountValidator validator;

    @Mock
    private ClientGateway clientGateway;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(accountService, "self", accountService);
    }

    @Test
    void createAccount_success() {
        CreateAccountRequest request = new CreateAccountRequest(1L, AccountType.SAVINGS, new BigDecimal("100"));
        when(clientGateway.exists(1L)).thenReturn(true);
        when(accountNumberGenerator.generate(AccountType.SAVINGS)).thenReturn("SVG-123456");
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.createAccount(request);

        assertThat(response.getAccountNumber()).isEqualTo("SVG-123456");
        assertThat(response.getBalance()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void createAccount_clientNotExists_throwsException() {
        CreateAccountRequest request = new CreateAccountRequest(99L, AccountType.SAVINGS, new BigDecimal("50"));
        when(clientGateway.exists(99L)).thenReturn(false);

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no existe");
    }

    @Test
    void deleteAccount_withBalanceNotZero_throwsException() {
        Account account = Account.builder()
                .id(1L)
                .accountNumber("CH-111111")
                .balance(new BigDecimal("50"))
                .clientId(1L)
                .type(AccountType.CHECKING)
                .active(true)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.delete(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("saldo distinto de 0");
    }

    @Test
    void activateAccount_success() {
        Account account = Account.builder()
                .id(1L).accountNumber("CH-222222").balance(BigDecimal.ZERO)
                .clientId(1L).type(AccountType.CHECKING).active(false).build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.activate(1L);

        assertThat(response.isActive()).isTrue();
    }

    @Test
    void deactivateAccount_success() {
        Account account = Account.builder()
                .id(1L).accountNumber("SVG-333333").balance(BigDecimal.ZERO)
                .clientId(1L).type(AccountType.SAVINGS).active(true).build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.deactivate(1L);

        assertThat(response.isActive()).isFalse();
    }

    @Test
    void get_accountNotFound_throwsException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.get(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cuenta no encontrada");
    }
}
