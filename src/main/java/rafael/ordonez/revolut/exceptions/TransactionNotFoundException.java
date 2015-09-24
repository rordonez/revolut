package rafael.ordonez.revolut.exceptions;

import org.hibernate.TransactionException;

/**
 * Created by rafa on 24/9/15.
 */
public class TransactionNotFoundException extends TransactionException {
    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
