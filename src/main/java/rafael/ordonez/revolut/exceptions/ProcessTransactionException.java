package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 24/9/15.
 */
public class ProcessTransactionException extends RuntimeException {
    public ProcessTransactionException() {
        super();
    }

    public ProcessTransactionException(String message) {
        super(message);
    }
}
