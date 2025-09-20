package org.banking.accountms.service;

import java.security.SecureRandom;
import lombok.AllArgsConstructor;
import org.banking.accountms.model.AccountType;
import org.banking.accountms.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountNumberGenerator {

    private final SecureRandom secureRandom = new SecureRandom();
    private final AccountRepository accountRepository;

    public String generate(AccountType type) {
        String prefix;
        switch (type) {
            case SAVINGS:
                prefix = "SVG-";
                break;
            case CHECKING:
                prefix = "CH-";
                break;
            default:
                prefix = "ACC-"; // fallback
        }

        String acc;
        do {
            acc = prefix + tenDigits();
        } while (accountRepository.existsByAccountNumber(acc));
        return acc;
    }

    private String tenDigits() {
        long high = secureRandom.nextInt(1_000_000);
        long low  = secureRandom.nextInt(10_000);
        return String.format("%06d%04d", high, low);
    }
}
