package ru.otus.hw09.core.service;


import ru.otus.hw09.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

    long saveAccount(Account account);

    Optional<Account> getAccount(long id);
}
