package rafael.ordonez.revolut.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rafael.ordonez.revolut.model.accounts.Account;

/**
 * Created by rafa on 23/9/15.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
