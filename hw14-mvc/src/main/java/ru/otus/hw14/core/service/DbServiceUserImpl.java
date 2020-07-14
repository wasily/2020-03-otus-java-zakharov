package ru.otus.hw14.core.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw14.cachehw.HwCache;
import ru.otus.hw14.core.dao.UserDao;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.sessionmanager.SessionManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;
    private final HwCache<String, User> cache;

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

    @Override
    public List<User> getAllUsers() {
        logger.info("Using getAllUsers");
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.findAll();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteUser(long id) {
        logger.info("Using deleteUser");
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.deleteUserById(id);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
        }
    }

    @Override
    public Optional<User> findRandomUser() {
        logger.info("Using findRandomUser");
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findRandomUser();
                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        logger.info("Using findByLogin");
        String cacheKey = "findByLogin" + login;
        var cachedUser = queryCache(cacheKey);
        if (cachedUser != null) {
            logger.info("cache hit");
            return Optional.of(cachedUser);
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findByLogin(login);
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

    private Optional<User> findUser(long id, String cacheKey, Function<Long, Optional<User>> function) {
        var cachedUser = queryCache(cacheKey);
        if (cachedUser != null) {
            logger.info("cache hit");
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
