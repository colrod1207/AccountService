package org.banking.accountms.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.banking.accountms.domain.model.Account;
import org.banking.accountms.domain.port.IAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryJpaAdapter implements IAccountRepository {

    private final AccountRepositoryJpa jpaRepository;

    @Override
    public Account save(Account account) {
        return AccountMapper.toDomain(
                jpaRepository.save(AccountMapper.toEntity(account))
        );
    }

    @Override
    public Optional<Account> findById(Long id) {
        return jpaRepository.findById(id).map(AccountMapper::toDomain);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpaRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll().stream()
                .map(AccountMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> findByClientId(Long clientId) {
        return jpaRepository.findByClientId(clientId).stream()
                .map(AccountMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Account account) {
        jpaRepository.delete(AccountMapper.toEntity(account));
    }
}
