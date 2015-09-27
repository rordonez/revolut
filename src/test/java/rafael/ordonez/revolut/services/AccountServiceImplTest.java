package rafael.ordonez.revolut.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rafael.ordonez.revolut.RevolutApplication;
import rafael.ordonez.revolut.exceptions.InternalAccountNotFoundException;
import rafael.ordonez.revolut.model.accounts.Account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rafa on 27/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
public class AccountServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private AccountService accountService;

    @Test
    public void testGetUserAccount() throws Exception {
        String accountNumber = "0";

        Account userAccount = accountService.getUserAccount(accountNumber);

        assertNotNull(userAccount);
        assertEquals(1L, userAccount.getId().longValue());
    }

    @Test(expected = InternalAccountNotFoundException.class)
    public void testGetUserAccountNotFound() throws Exception {
        String accountNumber = "1";

        accountService.getUserAccount(accountNumber);
    }


    @Test
    public void testGetInternalAccount() throws Exception {
        String accountNumber = "1";

        Account targetAccount = accountService.getInternalAccount(accountNumber);

        assertNotNull(targetAccount);
        assertEquals(2L, targetAccount.getId().longValue());
    }
}
