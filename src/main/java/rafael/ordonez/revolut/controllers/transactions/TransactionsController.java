package rafael.ordonez.revolut.controllers.transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.services.TransferService;

import javax.validation.Valid;

/**
 * Created by rafa on 22/9/15.
 */
@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

    private TransferService transferService;

    final static Logger LOG = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    public TransactionsController(TransferService transferService) {
        this.transferService = transferService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<AccountTransfer>> createTransfer(@Valid @RequestBody TransferRequestBody request)
    {
        LOG.info("Creating a new transfer with source account: " + request.getSourceAccount() + ", target account: " + request.getTargetAccount() + " and amount: " + request.getAmount());
        AccountTransfer transfer = transferService.doTransfer(request.getSourceAccount(), request.getTargetAccount(), request.getAmount());
        Link transferLink = ControllerLinkBuilder.linkTo(this.getClass()).slash(transfer.getId()).withSelfRel();
        return new ResponseEntity<>(new Resource<>(transfer, transferLink), HttpStatus.ACCEPTED);
    }

}
