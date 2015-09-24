package rafael.ordonez.revolut.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rafael.ordonez.revolut.RevolutApplication;
import rafael.ordonez.revolut.exceptions.AccountNotFoundException;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rafa on 23/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
public class TransferServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private TransferService transferService;

    @Test
    public void testDoTransfer() throws Exception {
        String sourceAccount = "0";
        String targetAccount = "1";
        double amount = 10.0;

        AccountTransfer transfer = transferService.doTransfer(sourceAccount, targetAccount, amount);

        assertNotNull(transferService.findById(transfer.getId()));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(transferService.findById(1L));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDoTransferThrowAccountNotFoundExceptionIfSourceAccountIsNotFound() throws Exception {
        String sourceAccount = "10";
        String targetAccount = "1";
        double amount = 10.0;

        transferService.doTransfer(sourceAccount, targetAccount, amount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDoTransferThrowAccountNotFoundExceptionIfTargetAccountIsNotFound() throws Exception {
        String sourceAccount = "0";
        String targetAccount = "10";
        double amount = 10.0;

        transferService.doTransfer(sourceAccount, targetAccount, amount);
    }
}
