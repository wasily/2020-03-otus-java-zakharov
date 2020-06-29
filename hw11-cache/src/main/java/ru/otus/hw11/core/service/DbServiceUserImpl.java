package ru.otus.hw11.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw11.cachehw.HwCache;
import ru.otus.hw11.core.dao.UserDao;
import ru.otus.hw11.core.model.User;
import ru.otus.hw11.core.sessionmanager.SessionManager;

import java.util.Optional;
import java.util.function.Function;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;
    private final HwCache<String, User> cache;

    public DbServiceUserImpl(UserDao userDao) {
        this(userDao, null);
    }

    public DbServiceUserImpl(UserDao userDao, HwCache<String, User> cache) {
        this.userDao = userDao;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long userId = user.getId();
                sessionManager.commitSession();
                if (cache != null) {
                    cache.put(String.valueOf(userId), user);
                }
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
        String cacheKey = "withLinkedEntities" + id;
        logger.info("Using findByIdWithPhonesAndAddress");
        return findUser(id, cacheKey, userDao::findByIdWithPhonesAndAddress);
    }

    @Override
    public Optional<User> getUser(long id) {
        logger.info("Using findById");
        return findUser(id, String.valueOf(id), userDao::findById);
    }

    private Optional<User> findUser(long id, String cacheKey, Function<Long, Optional<User>> function) {
        var cachedUser = queryCache(cacheKey);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = function.apply(id);
                logger.info("user: {}", userOptional.orElse(null));
                putInCache(cacheKey, userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    private User queryCache(String key) {
        if (cache == null) {
            return null;
        }
        return cache.get(key);
    }

    private void putInCache(String key, User user) {
        if (cache != null && user != null) {
            cache.put(key, user);
        }
    }
}
