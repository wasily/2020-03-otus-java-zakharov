package ru.otus.hw11.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw11.core.dao.UserDao;
import ru.otus.hw11.core.model.User;
import ru.otus.hw11.core.sessionmanager.SessionManager;

import java.util.Optional;
import java.util.function.Function;

public class DbServiceUserImpl implements DBServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long userId = user.getId();
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUserWithPhonesAndAddress(long id) {
        logger.info("Using findByIdWithPhonesAndAddress");
        return findUser(id, userDao::findByIdWithPhonesAndAddress);
    }

    @Override
    public Optional<User> getUser(long id) {
        logger.info("Using findById");
        return findUser(id, userDao::findById);
    }

    private Optional<User> findUser(long id, Function<Long, Optional<User>> function) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = function.apply(id);
                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
