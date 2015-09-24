package rafael.ordonez.revolut.model.accounts;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by rafa on 23/9/15.
 */
@Entity
@Table(name = "account")
public class Account implements Identifiable<Long>, Serializable {

    private static final long serialVersionUID = 2114898242170976167L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "alias")
    private String alias;

    @Column(name = "accountnumber", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Override
    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
