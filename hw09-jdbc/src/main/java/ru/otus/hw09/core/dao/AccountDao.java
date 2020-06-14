package ru.otus.hw09.core.dao;

import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface AccountDao {
    Optional<Account> findById(long id);

    long insertAccount(Account account);

    void updateAccount(Account account);

    void insertOrUpdate(Account account);

    SessionManager getSessionManager();
}
