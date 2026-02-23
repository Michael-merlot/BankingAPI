package bankingAPI.model;

import bankingAPI.entity.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "accounts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Builder.Default
    private String accountNumber = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 16)
            .toUpperCase();

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "RUB";

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> receivedTransactions = new ArrayList<>();

    public void deposit(BigDecimal amount){
        this.balance = this.balance.add(amount);
    }
    public void withdraw(BigDecimal amount){
        if (this.balance.compareTo(amount) < 0){
            throw new IllegalStateException("Недостаточно средств");
        }
        this.balance = this.balance.subtract(amount);
    }
}
