package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 23/9/15.
 */
public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
