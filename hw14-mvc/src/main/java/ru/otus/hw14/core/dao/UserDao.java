package ru.otus.hw14.core.dao;


import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    List<User> findAll();

    int deleteUserById(long id);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
