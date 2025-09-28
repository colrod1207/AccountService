package org.banking.accountms.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banking.accountms.domain.exception.Messages;
import org.banking.accountms.domain.exception.ResourceNotFoundException;
import org.banking.accountms.domain.factory.AccountFactoryProvider;
import org.banking.accountms.domain.model.Account;
import org.banking.accountms.domain.port.IAccountRepository;
import org.banking.accountms.domain.port.ClientsPort;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final IAccountRepository accountRepository;
    private final ClientsPort clientsPort;
    private final AccountValidator validator;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for client ID: {}", request.getClientId());

        validator.validate(request);

        if (!clientsPort.exists(request.getClientId())) {
            throw new IllegalArgumentException(Messages.CLIENT_NOT_FOUND + " (ID: " + request.getClientId() + ")");
        }

        String accountNumber = accountNumberGenerator.generate(request.getType());

        Account account = AccountFactoryProvider
                .getFactory(request.getType())
                .createAccount(request.getClientId(), request.getInitialBalance());

        account.assignAccountNumber(accountNumber);

        Account saved = accountRepository.save(account);
        log.info("Account created successfully: {}", saved.getAccountNumber());

        return AccountResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public AccountResponse get(Long accountId) {
        log.info("Fetching account with ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));

        return AccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listAll() {
        log.info("Listing all accounts");
        return accountRepository.findAll()
                .stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listByClient(Long clientId) {
        log.info("Listing accounts for client ID: {}", clientId);

        var accounts = accountRepository.findByClientId(clientId);

        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("El cliente no posee cuentas");
        }

        return accounts.stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long accountId) {
        log.info("Attempting to delete account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException(Messages.ACCOUNT_BALANCE_NOT_ZERO);
        }

        accountRepository.delete(account);
        log.info("Account deleted: {}", account.getAccountNumber());
    }

    @Transactional
    public AccountResponse activate(Long id) {
        log.info("Activating account ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));

        if (account.isActive()) {
            throw new ValidationException(Messages.ACCOUNT_ALREADY_ACTIVE);
        }

        account.activate();
        Account updated = accountRepository.save(account);

        return AccountResponse.from(updated);
    }

    @Transactional
    public AccountResponse deactivate(Long id) {
        log.info("Deactivating account ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));

        if (!account.isActive()) {
            throw new ValidationException(Messages.ACCOUNT_ALREADY_INACTIVE);
        }

        account.deactivate();
        Account updated = accountRepository.save(account);

        return AccountResponse.from(updated);
    }
}
