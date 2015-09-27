package rafael.ordonez.revolut.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafael.ordonez.revolut.exceptions.ProcessDuplicatedTransactionException;
import rafael.ordonez.revolut.exceptions.TransactionNotFoundException;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.model.transactions.AccountTransferStatus;
import rafael.ordonez.revolut.repositories.AccountRepository;
import rafael.ordonez.revolut.repositories.TransferRepository;


/**
 * Created by rafa on 23/9/15.
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    final static Logger LOG = LoggerFactory.getLogger(TransferServiceImpl.class);

    private TransferRepository transferRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public AccountTransfer doTransfer(long sourceAccountId, long targetAccountId, double amount) {
        LOG.info("Invoking doTransfer service...");
        return transferRepository.save(new AccountTransfer(sourceAccountId, targetAccountId, amount));
    }

    @Override
    public AccountTransfer processTransfer(long transactionId) {
        LOG.info("Processing the transaction with id: " + transactionId);
        AccountTransfer transfer = getTransaction(transactionId);

        accountRepository.setAmount(-transfer.getAmount(), transfer.getSourceAccountId());
        accountRepository.setAmount(transfer.getAmount(), transfer.getTargetAccountId());

        transfer.setStatus(AccountTransferStatus.COMPLETED);
        return transferRepository.save(transfer);
    }

    private AccountTransfer getTransaction(long transactionId) {
        AccountTransfer transfer = transferRepository.findOne(transactionId);
        if (transfer == null) {
            throw new TransactionNotFoundException("The transaction with id: " + transactionId + " is not found.");
        }
        if (transfer.getStatus().equals(AccountTransferStatus.COMPLETED)) {
            throw new ProcessDuplicatedTransactionException("The transaction with id: " + transactionId + "has been processed.");
        }
        return transfer;
    }
}
