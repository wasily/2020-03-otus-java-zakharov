package hw16.core.service;


import hw16.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();

    int deleteUser(long id);

}
