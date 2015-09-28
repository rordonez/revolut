package rafael.ordonez.revolut.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rafael.ordonez.revolut.model.accounts.Account;

/**
 * Created by rafa on 23/9/15.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("select a from Account a where  a.owner = :userId and a.accountNumber = :accountNumber")
    Account findByAccountNumber(@Param("userId") long userId, @Param("accountNumber") String accountNumber);

    Account findByAccountNumber(String accountNumber);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance - :amount where a.id = :id")
    int decreaseBy(@Param("amount") Double amount, @Param("id") long id);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance + :amount where a.id = :id")
    int increaseBy(@Param("amount") Double amount, @Param("id") long id);
}
