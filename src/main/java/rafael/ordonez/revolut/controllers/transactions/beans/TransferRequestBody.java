package rafael.ordonez.revolut.controllers.transactions.beans;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by rafa on 22/9/15.
 */
public class TransferRequestBody  implements Serializable {

    private static final long serialVersionUID = -4122376909275921245L;

    @NotNull
    private String sourceAccount;
    @NotNull
    private String targetAccount;
    @Min(value = 1)
    private double amount;

    public TransferRequestBody() {
    }

    public TransferRequestBody(String sourceAccount, String targetAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
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
