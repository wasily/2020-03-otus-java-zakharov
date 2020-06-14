package ru.otus.hw09.jdbc.dao;


import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.mapper.JdbcMapper;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class MappedUserDaoJdbc implements UserDao {
    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<User> jdbcMapper;

    public MappedUserDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<User> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(jdbcMapper.findById(id, User.class));
    }

    @Override
    public long insertUser(User user) {
        jdbcMapper.insert(user);
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        jdbcMapper.update(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        jdbcMapper.insertOrUpdate(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
