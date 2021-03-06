package hw16.core.service;

import hw16.cachehw.HwCache;
import hw16.core.dao.UserDao;
import hw16.core.model.User;
import hw16.core.sessionmanager.SessionManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
                putInCache(String.valueOf(userId), user);
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
    public Optional<User> getUser(long id) {
        return findUser(id, String.valueOf(id), userDao::findById);
    }

    @Override
    public List<User> getAllUsers() {
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
    public int deleteUser(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.deleteUserById(id);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
        }
        return -1;
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
