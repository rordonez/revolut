package rafael.ordonez.revolut.repositories;

import org.springframework.data.repository.CrudRepository;
import rafael.ordonez.revolut.model.user.User;

/**
 * Created by rafa on 27/9/15.
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
