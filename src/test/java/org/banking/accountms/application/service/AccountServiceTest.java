package org.banking.accountms.application.service;

import org.banking.accountms.domain.exception.Messages;
import org.banking.accountms.domain.exception.ResourceNotFoundException;
import org.banking.accountms.domain.factory.AccountFactoryProvider;
import org.banking.accountms.domain.model.Account;
import org.banking.accountms.domain.model.AccountType;
import org.banking.accountms.domain.port.ClientsPort;
import org.banking.accountms.domain.port.IAccountRepository;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private ClientsPort clientsPort;

    @Mock
    private AccountValidator validator;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private AccountService accountService;

    private CreateAccountRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateAccountRequest();
        request.setClientId(1L);
        request.setType(AccountType.SAVINGS);
        request.setInitialBalance(BigDecimal.TEN);
    }

    // --- CREATE ---
    @Test
    void createAccount_success() {
        when(clientsPort.exists(1L)).thenReturn(true);
        when(accountNumberGenerator.generate(AccountType.SAVINGS)).thenReturn("ACC123");
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponse response = accountService.createAccount(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccountNumber()).isEqualTo("ACC123");
        verify(validator).validate(request);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_clientNotFound() {
        when(clientsPort.exists(1L)).thenReturn(false);

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Messages.CLIENT_NOT_FOUND);
    }

    // --- GET ---
    @Test
    void get_success() {
        Account account = AccountFactoryProvider.getFactory(AccountType.SAVINGS)
                .createAccount(1L, BigDecimal.TEN);
        account.assignAccountNumber("ACC123");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.get(1L);

        assertThat(response.getAccountNumber()).isEqualTo("ACC123");
    }

    @Test
    void get_notFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.get(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(Messages.ACCOUNT_NOT_FOUND);
    }

    // --- LIST ---
    @Test
    void listAll_success() {
        Account account = AccountFactoryProvider.getFactory(AccountType.SAVINGS)
                .createAccount(1L, BigDecimal.TEN);
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountResponse> responses = accountService.listAll();

        assertThat(responses).hasSize(1);
    }

    @Test
    void listByClient_success() {
        Account account = AccountFactoryProvider.getFactory(AccountType.SAVINGS)
                .createAccount(1L, BigDecimal.TEN);
        when(accountRepository.findByClientId(1L)).thenReturn(List.of(account));

        List<AccountResponse> responses = accountService.listByClient(1L);

        assertThat(responses).hasSize(1);
    }

    @Test
    void listByClient_notFound() {
        when(accountRepository.findByClientId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> accountService.listByClient(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("El cliente no posee cuentas");
    }

    // --- DELETE ---
    @Test
    void delete_success() {
        Account account = AccountFactoryProvider.getFactory(AccountType.SAVINGS)
                .createAccount(1L, BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.delete(1L);

        verify(accountRepository).delete(account);
    }

    @Test
    void delete_balanceNotZero() {
        Account account = AccountFactoryProvider.getFactory(AccountType.SAVINGS)
                .createAccount(1L, BigDecimal.TEN);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.delete(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessage(Messages.ACCOUNT_BALANCE_NOT_ZERO);
    }

    @Test
    void delete_notFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- ACTIVATE ---
    @Test
    void activate_success() {
        Account account = new Account(1L, "ACC1", BigDecimal.ZERO, AccountType.SAVINGS, 1L, false);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        AccountResponse response = accountService.activate(1L);

        assertThat(response.isActive()).isTrue();
    }

    @Test
    void activate_alreadyActive() {
        Account account = new Account(1L, "ACC1", BigDecimal.ZERO, AccountType.SAVINGS, 1L, true);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.activate(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessage(Messages.ACCOUNT_ALREADY_ACTIVE);
    }

    // --- DEACTIVATE ---
    @Test
    void deactivate_success() {
        Account account = new Account(1L, "ACC1", BigDecimal.ZERO, AccountType.SAVINGS, 1L, true);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        AccountResponse response = accountService.deactivate(1L);

        assertThat(response.isActive()).isFalse();
    }

    @Test
    void deactivate_alreadyInactive() {
        Account account = new Account(1L, "ACC1", BigDecimal.ZERO, AccountType.SAVINGS, 1L, false);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.deactivate(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessage(Messages.ACCOUNT_ALREADY_INACTIVE);
    }
}
