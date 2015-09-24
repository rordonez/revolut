package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 24/9/15.
 */
public class ProcessDuplicatedTransactionException extends ProcessTransactionException {
    public ProcessDuplicatedTransactionException(String message) {
        super(message);
    }
}
