package rafael.ordonez.revolut.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.repositories.TransferRepository;

import javax.transaction.Transactional;

/**
 * Created by rafa on 23/9/15.
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public AccountTransfer doTransfer(String sourceAccount, String targetAccount, double amount) {
        AccountTransfer  transfer = new AccountTransfer(sourceAccount, targetAccount, amount);
        AccountTransfer result = transferRepository.save(transfer);
        return transferRepository.findOne(result.getId());
    }
}
