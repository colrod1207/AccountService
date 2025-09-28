package org.banking.accountms.domain.port;

import org.banking.accountms.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository {

    Account save(Account account);

    Optional<Account> findById(Long id);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findAll();

    List<Account> findByClientId(Long clientId);

    void delete(Account account);
}
