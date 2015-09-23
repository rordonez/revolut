package rafael.ordonez.revolut.model.transactions;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by rafa on 22/9/15.
 */
@Entity
@Table(name ="transaction")
public class AccountTransfer implements Identifiable<Long>, Serializable {

    private static final long serialVersionUID = 691422037793998175L;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "sourceAccount", nullable = false)
    private String sourceAccount;

    @Column(name = "targetAccount", nullable = false)
    private String targetAccount;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "status")
    private AccountTransferStatus status;

    public AccountTransfer() {
    }

    public AccountTransfer(String sourceAccount, String targetAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
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

    public String getSourceAccount() {
        return sourceAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public double getAmount() {
        return amount;
    }
}
