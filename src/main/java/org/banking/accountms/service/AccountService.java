package org.banking.accountms.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banking.accountms.adapter.ClientGateway;
import org.banking.accountms.common.Messages;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.exception.ResourceNotFoundException;
import org.banking.accountms.mapper.AccountMapper;
import org.banking.accountms.model.Account;
import org.banking.accountms.repository.AccountRepository;
import org.banking.accountms.service.factory.AccountFactoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountValidator validator;
    private final ClientGateway clientGateway;
    private final AccountNumberGenerator accountNumberGenerator;

    private AccountService self;

    @Autowired
    public void setSelf(@Lazy AccountService self) {
        this.self = self;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        validator.validate(request);

        if (!clientGateway.exists(request.getClientId())) {
            throw new IllegalArgumentException("El cliente con ID " + request.getClientId() + " no existe.");
        }

        validator.validate(request);

        String accountNumber = accountNumberGenerator.generate(request.getType());

        Account account = AccountFactoryProvider
                .getFactory(request.getType())
                .createAccount(request.getClientId(), request.getInitialBalance());

        account.setAccountNumber(accountNumber);

        accountRepository.save(account);
        log.info("Cuenta creada con factory: {}", account.getAccountNumber());
        return AccountMapper.toResponse(account);
    }

    @Transactional(readOnly = true)
    public Account get(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listAll() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AccountResponse> listByClient(Long clientId) {
        return accountRepository.findByClientId(clientId)
                .stream()
                .map(account -> AccountResponse.builder()
                        .id(account.getId())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .type(account.getType())
                        .clientId(account.getClientId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long accountId) {
        Account account = self.get(accountId);
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException(Messages.ACCOUNT_BALANCE_NOT_ZERO);
        }
        accountRepository.delete(account);
        log.info("Cuenta eliminada: {}", account.getAccountNumber());
    }

    public AccountResponse activate(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));
        if (account.isActive()) {
            throw new ValidationException(Messages.ACCOUNT_ALREADY_ACTIVE);
        }
        account.setActive(true);
        Account updated = accountRepository.save(account);
        return AccountMapper.toResponse(updated);
    }

    public AccountResponse deactivate(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.ACCOUNT_NOT_FOUND));
        if (!account.isActive()) {
            throw new ValidationException(Messages.ACCOUNT_ALREADY_INACTIVE);
        }
        account.setActive(false);
        Account updated = accountRepository.save(account);
        return AccountMapper.toResponse(updated);
    }

}
