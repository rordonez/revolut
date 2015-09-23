package rafael.ordonez.revolut.controllers.errorhandling;

import java.io.Serializable;

/**
 * Created by rafa on 23/9/15.
 */
public class ErrorNode implements Serializable {

    private static final long serialVersionUID = 8019609330142487169L;
    private String message;

    public ErrorNode() {
    }

    public ErrorNode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
