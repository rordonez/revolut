package rafael.ordonez.revolut.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafael.ordonez.revolut.exceptions.InternalAccountNotFoundException;
import rafael.ordonez.revolut.model.accounts.Account;
import rafael.ordonez.revolut.repositories.AccountRepository;

/**
 * Created by rafa on 27/9/15.
 */
@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private UserService userService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @Override
    public Account getUserAccount(String accountNumber) {
        long userId = userService.getCurrentUserId();
        Account userAccount = accountRepository.findByAccountNumber(userId, accountNumber);
        if (userAccount == null) {
            throw new InternalAccountNotFoundException("The source account with number: " + accountNumber + " does not belong to the current user");
        }
        return userAccount;
    }

    @Override
    public Account getInternalAccount(String accountNumber) {
        Account targetAccount = accountRepository.findByAccountNumber(accountNumber);
        return targetAccount;
    }
}
