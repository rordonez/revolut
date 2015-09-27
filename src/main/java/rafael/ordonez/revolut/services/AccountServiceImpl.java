package rafael.ordonez.revolut.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafael.ordonez.revolut.model.accounts.Account;

/**
 * Created by rafa on 27/9/15.
 */
@Transactional
@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public Account getUserAccount(String accountNumber) {
        return null;
    }

    @Override
    public Account getInternalAccount(String accountNumber) {
        return null;
    }
}
