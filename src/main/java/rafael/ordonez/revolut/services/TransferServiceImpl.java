package rafael.ordonez.revolut.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rafael.ordonez.revolut.exceptions.AccountNotFoundException;
import rafael.ordonez.revolut.model.accounts.Account;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.repositories.AccountRepository;
import rafael.ordonez.revolut.repositories.TransferRepository;

import javax.transaction.Transactional;

/**
 * Created by rafa on 23/9/15.
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {


    private TransferRepository transferRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountTransfer doTransfer(String sourceAccountNumber, String targetAccountNumber, double amount) {
        Account sourceAccount = getAccount(sourceAccountNumber);
        Account targetAccount = getAccount(targetAccountNumber);

        AccountTransfer result = transferRepository.save(new AccountTransfer(sourceAccount.getId(), targetAccount.getId(), amount));
        return transferRepository.findOne(result.getId());
    }

    @Override
    public AccountTransfer findById(Long id) {
        return transferRepository.findOne(id);
    }

    private Account getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("The account with number: " + accountNumber + " is not found.");
        }
        return account;
    }
}
