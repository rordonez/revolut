package rafael.ordonez.revolut.model.transactions;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by rafa on 22/9/15.
 */
@Entity
@Table(name ="transfer")
public class AccountTransfer implements Identifiable<Long>, Serializable {

    private static final long serialVersionUID = 691422037793998175L;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "sourceaccount", nullable = false)
    private Long sourceAccountId;

    @Column(name = "targetaccount", nullable = false)
    private Long targetAccountId;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountTransferStatus status;

    public AccountTransfer() {
    }

    public AccountTransfer(Long sourceAccountId, Long targetAccountId, double amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.status = AccountTransferStatus.PENDING;
    }

    public AccountTransferStatus getStatus() {
        return status;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public double getAmount() {
        return amount;
    }
}
