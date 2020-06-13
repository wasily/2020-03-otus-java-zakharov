package ru.otus.hw09.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.DbExecutorImpl;
import ru.otus.hw09.jdbc.mapper.JdbcMapper;
import ru.otus.hw09.jdbc.mapper.JdbcMapperImpl;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class MappedUserDaoJdbc implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(MappedUserDaoJdbc.class);
    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<User> jdbcMapper;

    public MappedUserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutorImpl<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = new JdbcMapperImpl<>(User.class, dbExecutor, sessionManager);
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
