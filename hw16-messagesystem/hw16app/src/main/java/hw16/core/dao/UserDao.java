package hw16.core.dao;



import hw16.core.model.User;
import hw16.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    List<User> findAll();

    int deleteUserById(long id);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
