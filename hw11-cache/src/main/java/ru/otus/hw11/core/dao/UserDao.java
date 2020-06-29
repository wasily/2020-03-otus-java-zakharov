package ru.otus.hw11.core.dao;


import ru.otus.hw11.core.model.User;
import ru.otus.hw11.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    Optional<User> findByIdWithPhonesAndAddress(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
