package rafael.ordonez.revolut.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rafa on 22/9/15.
 */
@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> getAccounts() {
        return null;
    }
}
