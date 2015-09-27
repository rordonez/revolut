package rafael.ordonez.revolut.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rafael.ordonez.revolut.model.accounts.Account;

/**
 * Created by rafa on 23/9/15.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("select a from Account a where  a.owner = ?1 and a.accountNumber = ?2")
    Account findByAccountNumber(long userId, String accountNumber);

    Account findByAccountNumber(String accountNumber);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance + :amount where a.id = :id")
    Integer setAmount(@Param("amount") Double amount, @Param("id") Long id);
}
