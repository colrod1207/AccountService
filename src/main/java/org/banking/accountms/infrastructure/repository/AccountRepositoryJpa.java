package org.banking.accountms.infrastructure.repository;

import org.banking.accountms.infrastructure.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepositoryJpa extends JpaRepository<AccountEntity, Long> {
    boolean existsByAccountNumber(String accountNumber);
    List<AccountEntity> findByClientId(Long clientId);
}
