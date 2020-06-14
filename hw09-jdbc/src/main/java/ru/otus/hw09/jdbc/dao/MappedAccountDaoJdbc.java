package ru.otus.hw09.jdbc.dao;


import ru.otus.hw09.core.dao.AccountDao;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.mapper.JdbcMapper;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class MappedAccountDaoJdbc implements AccountDao {
    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<Account> jdbcMapper;

    public MappedAccountDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        return Optional.ofNullable(jdbcMapper.findById(id, Account.class));
    }

    @Override
    public long insertAccount(Account account) {
        jdbcMapper.insert(account);
        return account.getNo();
    }

    @Override
    public void updateAccount(Account account) {
        jdbcMapper.update(account);
    }

    @Override
    public void insertOrUpdate(Account account) {
        jdbcMapper.insertOrUpdate(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
