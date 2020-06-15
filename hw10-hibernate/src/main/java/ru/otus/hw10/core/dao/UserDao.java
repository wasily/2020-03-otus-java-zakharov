package ru.otus.hw10.core.dao;


import ru.otus.hw10.core.model.User;
import ru.otus.hw10.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
