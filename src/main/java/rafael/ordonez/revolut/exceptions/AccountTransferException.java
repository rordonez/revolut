package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 23/9/15.
 */
public class AccountTransferException extends RuntimeException {
    public AccountTransferException() {
    }

    public AccountTransferException(String message) {
        super(message);
    }
}