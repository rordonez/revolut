package rafael.ordonez.revolut.controllers.transactions.beans;

import java.io.Serializable;

/**
 * Created by rafa on 22/9/15.
 */
public class TransferRequestBody  implements Serializable {

    private static final long serialVersionUID = -4122376909275921245L;

    private String sourceAccount;
    private String targetAccount;
    private double amount;

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

}
