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
import rafael.ordonez.revolut.exceptions.AccountTransferException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rafa on 23/9/15.
 */
@ControllerAdvice
public class RevolutControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountTransferException.class)
    @ResponseBody
    public ResponseEntity<ErrorNode> handleInternalTransferException(AccountTransferException e) {
        return new ResponseEntity<>(new ErrorNode(e.getMessage()), generateHeaders(), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(getErrors(ex), generateHeaders(), HttpStatus.BAD_REQUEST);
    }

    private List<ErrorNode> getErrors(MethodArgumentNotValidException ex) {
        return Stream.of(ex.getBindingResult().getAllErrors())
                    .map(x -> new ErrorNode("Invalid value for argument " + ((FieldError) x).getField()))
                    .collect(Collectors.toList());
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
