package org.banking.accountms.infrastructure.entity;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import org.banking.accountms.domain.model.AccountType;

@Entity
@Table(name = "accounts",
        indexes = {
                @Index(name = "idx_accounts_account_number_unique", columnList = "accountNumber", unique = true),
                @Index(name = "idx_accounts_client_id", columnList = "client_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private boolean active;
}
