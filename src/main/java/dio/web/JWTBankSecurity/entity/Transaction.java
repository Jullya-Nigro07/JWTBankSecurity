package dio.web.JWTBankSecurity.entity;

import dio.web.JWTBankSecurity.enums.TipoTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransaction type;

    private BigDecimal amount;
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction(TipoTransaction type, BigDecimal amount, Account account){
        this.type = type;
        this.amount = amount;
        this.account = account;
    }

    public Transaction() {
    }

    @PrePersist
    public void prePersist() {
        this.dateTime = LocalDateTime.now();
    }
}