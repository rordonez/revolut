package rafael.ordonez.revolut.controllers.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.services.TransferService;

/**
 * Created by rafa on 22/9/15.
 */
@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

    private TransferService transferService;

    @Autowired
    public TransactionsController(TransferService transferService) {
        this.transferService = transferService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<AccountTransfer>> createTransfer(@RequestBody TransferRequestBody request)
    {
        AccountTransfer transfer = transferService.doTransfer(request.getSourceAccount(), request.getTargetAccount(), request.getAmount());
        return new ResponseEntity<>(new Resource<>(transfer), HttpStatus.ACCEPTED);
    }

}