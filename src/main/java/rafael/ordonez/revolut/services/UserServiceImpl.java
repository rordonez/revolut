package rafael.ordonez.revolut.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafael.ordonez.revolut.repositories.UserRepository;

/**
 * Created by rafa on 27/9/15.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * Assumption: When we have a security framework, we could obtain the current user from the access token.
     * For this application, the user with id 0 is the current user.
     *
     * @return
     */
    @Override
    public long getCurrentUserId() {
        return userRepository.findOne(1L).getId();
    }
}
