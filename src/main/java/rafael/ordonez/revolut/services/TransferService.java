package rafael.ordonez.revolut.services;

import rafael.ordonez.revolut.model.transactions.AccountTransfer;

/**
 * Created by rafa on 22/9/15.
 */
public interface TransferService {
    AccountTransfer doTransfer(String sourceAccountNumber, String targetAccountNumber, double amount);

    AccountTransfer findById(Long id);

}
