package rafael.ordonez.revolut.controllers.errorhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rafael.ordonez.revolut.exceptions.InternalAccountNotFoundException;
import rafael.ordonez.revolut.exceptions.ProcessTransactionException;
import rafael.ordonez.revolut.exceptions.TransactionNotImplementedException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rafa on 23/9/15.
 */
@ControllerAdvice
public class RevolutControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ProcessTransactionException.class, InternalAccountNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorNode> handleBadRequest(Exception e) {
        return new ResponseEntity<>(new ErrorNode(e.getMessage()), generateHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TransactionNotImplementedException.class})
    @ResponseBody
    public ResponseEntity<ErrorNode> handleNotImplemented(Exception e) {
        return new ResponseEntity<>(new ErrorNode(e.getMessage()), generateHeaders(), HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(getErrors(ex), generateHeaders(), HttpStatus.BAD_REQUEST);
    }

    private List<ErrorNode> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors()
                .stream()
                .map(x -> new ErrorNode("Invalid value for argument " + ((FieldError) x).getField() + " and description: " + x.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
