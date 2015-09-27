package rafael.ordonez.revolut.controllers.transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.services.AccountService;
import rafael.ordonez.revolut.services.TransferService;

import javax.validation.Valid;

/**
 * Created by rafa on 22/9/15.
 */
@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

    private TransferService transferService;

    private AccountService accountService;

    final static Logger LOG = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    public TransactionsController(TransferService transferService, AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<AccountTransfer>> createTransfer(@Valid @RequestBody TransferRequestBody request)
    {
        LOG.info("Creating a new transfer with source account: " + request.getSourceAccount() + ", target account: " + request.getTargetAccount() + " and amount: " + request.getAmount());
        accountService.getUserAccount(request.getSourceAccount());
        accountService.isInternal(request.getTargetAccount());
        AccountTransfer transfer = transferService.doTransfer(request.getSourceAccount(), request.getTargetAccount(), request.getAmount());
        Link transferLink = ControllerLinkBuilder.linkTo(this.getClass()).slash(transfer.getId()).withSelfRel();
        return new ResponseEntity<>(new Resource<>(transfer, transferLink), HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{transferId}")
    public ResponseEntity<Resource<AccountTransfer>> processTransfer(@PathVariable long transferId) {
        LOG.info("Processing the transaction with id " + transferId);
        AccountTransfer transfer = transferService.processTransfer(transferId);
        Link transferLink = ControllerLinkBuilder.linkTo(this.getClass()).slash(transfer.getId()).withSelfRel();
        return new ResponseEntity<>(new Resource<>(transfer, transferLink), HttpStatus.OK);
    }

}
