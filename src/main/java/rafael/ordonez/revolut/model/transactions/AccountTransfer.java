package rafael.ordonez.revolut.model.transactions;

import java.io.Serializable;

/**
 * Created by rafa on 22/9/15.
 */
public class AccountTransfer implements Serializable{

    private static final long serialVersionUID = 691422037793998175L;

    private long id;
    private AccountTransferStatus status;

    public AccountTransfer() {
    }

    public AccountTransfer(long id, AccountTransferStatus status) {
        this.id = id;
        this.status = status;
    }

    public AccountTransferStatus getStatus() {
        return status;
    }

    public long getId() {
        return id;
    }

}
