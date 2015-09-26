package rafael.ordonez.revolut.exceptions;

/**
 * Created by rafa on 26/9/15.
 */
public class InternalAccountNotFoundException extends RuntimeException {
    public InternalAccountNotFoundException(String message) {
        super(message);
    }
}
