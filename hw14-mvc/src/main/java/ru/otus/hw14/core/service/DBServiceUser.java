package ru.otus.hw14.core.service;

import ru.otus.hw14.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();

    void deleteUser(long id);

    Optional<User> getUserWithPhonesAndAddress(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
