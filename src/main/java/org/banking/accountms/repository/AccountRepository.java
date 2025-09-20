package org.banking.accountms.repository;

import java.util.List;
import org.banking.accountms.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByClientId(Long clientId);
}
