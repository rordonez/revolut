package rafael.ordonez.revolut.model.transactions;

import java.io.Serializable;

/**
 * Created by rafa on 22/9/15.
 */
public class AccountTransfer implements Serializable{

    private static final long serialVersionUID = 691422037793998175L;

    private Status status;

    public AccountTransfer() {
    }

    public Status getStatus() {
        return status;
    }

    enum Status { PENDING, COMPLETED}
}
