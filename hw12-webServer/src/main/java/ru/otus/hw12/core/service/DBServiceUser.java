package ru.otus.hw12.core.service;

import ru.otus.hw12.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUserWithPhonesAndAddress(long id);
}
