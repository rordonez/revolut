package rafael.ordonez.revolut.controllers.transactions;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;

/**
 * Created by rafa on 22/9/15.
 */
@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<AccountTransfer>> createTransfer(@RequestBody TransferRequestBody request)
    {
        return new ResponseEntity<>(new Resource<>(new AccountTransfer()), HttpStatus.ACCEPTED);
    }

}
