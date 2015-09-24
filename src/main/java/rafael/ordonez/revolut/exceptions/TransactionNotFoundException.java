package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 24/9/15.
 */
public class TransactionNotFoundException extends ProcessTransactionException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
