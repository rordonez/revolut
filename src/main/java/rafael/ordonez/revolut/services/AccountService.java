package rafael.ordonez.revolut.services;

import rafael.ordonez.revolut.model.accounts.Account;

/**
 * Created by rafa on 25/9/15.
 */
public interface AccountService {

    Account getUserAccount(String accountNumber);

    Account getInternalAccount(String accountNumber);

}
