package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 27/9/15.
 */
public class TransactionNotImplementedException extends RuntimeException {
    public TransactionNotImplementedException(String message) {
        super(message);
    }
}
