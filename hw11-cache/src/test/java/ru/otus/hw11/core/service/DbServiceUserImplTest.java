package ru.otus.hw11.core.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw11.cachehw.HwCache;
import ru.otus.hw11.cachehw.MyCache;
import ru.otus.hw11.core.dao.UserDao;
import ru.otus.hw11.core.model.AddressDataSet;
import ru.otus.hw11.core.model.PhoneDataSet;
import ru.otus.hw11.core.model.User;
import ru.otus.hw11.hibernate.HibernateUtils;
import ru.otus.hw11.hibernate.dao.UserDaoHibernate;
import ru.otus.hw11.hibernate.sessionmanager.SessionManagerHibernate;

import java.time.LocalTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DbServiceUserImplTest {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImplTest.class);
    private static final int ROWS_COUNT = 100;
    private static final int QUERY_COUNT = 10_000;

    private SessionFactory sessionFactory;
    private DbServiceUserImpl dbServiceUser;
    private DbServiceUserImpl cachedDbServiceUser;
    private final HwCache<String, User> myCache = new MyCache<>();

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory("hibernate-test.cfg.xml",
                User.class, AddressDataSet.class, PhoneDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
        cachedDbServiceUser = new DbServiceUserImpl(userDao, myCache);
    }

    @AfterEach
    void clearUsersTable() {
        sessionFactory.close();
    }

    @Test
    void shouldPutInCacheWhenSaving() {
        var address = new AddressDataSet(UUID.randomUUID().toString(), "street name");
        var phonesSet = Set.of(new PhoneDataSet(UUID.randomUUID().toString(), "222-22-22"),
                new PhoneDataSet(UUID.randomUUID().toString(), "333-33-33"));
        var user = new User(0, "username", phonesSet, address);

        assertEquals(0, myCache.getSize());
        cachedDbServiceUser.saveUser(user);
        assertEquals(1, myCache.getSize());

        var userFromCache = myCache.get(String.valueOf(user.getId()));
        assertThat(user).isEqualToComparingFieldByField(userFromCache);
    }

    @Test
    void shouldTestThatCacheStoresObjectsForDifferentGetUserMethodsIndependently() {
        var address = new AddressDataSet(UUID.randomUUID().toString(), "street name");
        var phonesSet = Set.of(new PhoneDataSet(UUID.randomUUID().toString(), "222-22-22"),
                new PhoneDataSet(UUID.randomUUID().toString(), "333-33-33"));
        var user = new User(0, "username", phonesSet, address);

        dbServiceUser.saveUser(user);
        assertEquals(0, myCache.getSize());

        cachedDbServiceUser.getUser(user.getId());
        assertEquals(1, myCache.getSize());
        myCache.remove(String.valueOf(user.getId()));

        cachedDbServiceUser.getUserWithPhonesAndAddress(user.getId());
        assertEquals(1, myCache.getSize());
    }

    @Test
    void shouldProveThatGCDropsCache() throws InterruptedException {
        fillUsersTableWithSampleData(cachedDbServiceUser, 10_000);
        logger.warn("cache size: {}", myCache.getSize());
        assertNotEquals(0, myCache.getSize());
        System.gc();
        Thread.sleep(100);
        logger.warn("cache size after GC: {}", myCache.getSize());
        assertEquals(0, myCache.getSize());
    }

    @Test
    void shouldProveThatCachedServiceIsAtLeastFiveTimesFasterThanNonCached() {
        fillUsersTableWithSampleData(dbServiceUser, ROWS_COUNT);
        long nonCachedTime = dbServiceKindOfBenchmark(dbServiceUser);
        long cachedTime = dbServiceKindOfBenchmark(cachedDbServiceUser);
        logger.info("non-cached: {}", nonCachedTime);
        logger.info("cached: {}", cachedTime);
        Assertions.assertTrue(nonCachedTime > cachedTime * 5);
    }

    private static void fillUsersTableWithSampleData(DBServiceUser dbServiceUser, int rowsNumber) {
        for (int i = 0; i < rowsNumber; i++) {
            dbServiceUser.saveUser(new User(0, "username#" + i, null, null));
        }
    }

    private static long dbServiceKindOfBenchmark(DBServiceUser dbServiceUser) {
        LocalTime before = LocalTime.now();
        for (int i = 0; i < QUERY_COUNT; i++) {
            dbServiceUser.getUser(new Random().nextInt(ROWS_COUNT) + 1);
        }
        LocalTime after = LocalTime.now();
        return MILLIS.between(before, after);
    }
}