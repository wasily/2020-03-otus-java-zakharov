package ru.otus.hw12.core.dao;


import ru.otus.hw12.core.model.User;
import ru.otus.hw12.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    List<User> findAll();

    Optional<User> findByIdWithPhonesAndAddress(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);

    SessionManager getSessionManager();
}
