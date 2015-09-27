package rafael.ordonez.revolut.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rafael.ordonez.revolut.RevolutApplication;
import rafael.ordonez.revolut.exceptions.ProcessDuplicatedTransactionException;
import rafael.ordonez.revolut.exceptions.TransactionNotFoundException;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.model.transactions.AccountTransferStatus;
import rafael.ordonez.revolut.repositories.AccountRepository;
import rafael.ordonez.revolut.repositories.TransferRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rafa on 23/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
public class TransferServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Test
    public void testDoTransfer() throws Exception {
        long sourceAccount = 1L;
        long targetAccount = 2L;
        double amount = 10.0;

        AccountTransfer transfer = transferService.doTransfer(sourceAccount, targetAccount, amount);

        assertNotNull(transferRepository.findOne(transfer.getId()));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(transferRepository.findOne(1L));
    }

    @Test
    public void testProcessTransaction() throws Exception {
        long transactionId = 1L;
        AccountTransfer pendingTransfer = transferRepository.findOne(transactionId);
        double sourceAccountBalance = accountRepository.findOne(pendingTransfer.getSourceAccountId()).getBalance();
        double targetAccountBalance = accountRepository.findOne(pendingTransfer.getTargetAccountId()).getBalance();

        AccountTransfer transfer = transferService.processTransfer(transactionId);
        double sourceAccountBalanceAfterTransaction = accountRepository.findOne(transfer.getSourceAccountId()).getBalance();
        double targetAccountBalanceAfterTransaction = accountRepository.findOne(transfer.getTargetAccountId()).getBalance();

        assertEquals(AccountTransferStatus.COMPLETED, transfer.getStatus());
        assertThat(sourceAccountBalanceAfterTransaction, equalTo(sourceAccountBalance - pendingTransfer.getAmount()));
        assertThat(targetAccountBalanceAfterTransaction, equalTo(targetAccountBalance + pendingTransfer.getAmount()));
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testProcessTransactionThrowTransactionNotFoundException() throws Exception {
        long transactionId = 10L;
        transferService.processTransfer(transactionId);
    }

    @Test(expected = ProcessDuplicatedTransactionException.class)
    public void testProcessTransactionTwice() throws Exception {
        long transactionId = 1L;

        transferService.processTransfer(transactionId);
        transferService.processTransfer(transactionId);
    }


}
