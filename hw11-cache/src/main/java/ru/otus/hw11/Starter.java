package ru.otus.hw11;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw11.cachehw.HwCache;
import ru.otus.hw11.cachehw.HwListener;
import ru.otus.hw11.cachehw.MyCache;
import ru.otus.hw11.core.dao.UserDao;
import ru.otus.hw11.core.model.AddressDataSet;
import ru.otus.hw11.core.model.PhoneDataSet;
import ru.otus.hw11.core.model.User;
import ru.otus.hw11.core.service.DBServiceUser;
import ru.otus.hw11.core.service.DbServiceUserImpl;
import ru.otus.hw11.hibernate.HibernateUtils;
import ru.otus.hw11.hibernate.dao.UserDaoHibernate;
import ru.otus.hw11.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Set;
import java.util.UUID;

public class Starter {
    private static final Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);


        var address = new AddressDataSet(UUID.randomUUID().toString(), "some address");
        var phone = new PhoneDataSet(UUID.randomUUID().toString(), "1234567890");
        var phone2 = new PhoneDataSet(UUID.randomUUID().toString(), "123-1234-34");
        User user = new User(0, "username", Set.of(phone, phone2), address);
        dbServiceUser.saveUser(user);
        dbServiceUser.getUser(user.getId());
        dbServiceUser.getUserWithPhonesAndAddress(user.getId());

        logger.info("Started using MyCache");
        HwCache<String, User> myCache = new MyCache<>();
        HwListener<String, User> listener = new HwListener<>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        myCache.addListener(listener);
        DBServiceUser cachedDbServiceUser = new DbServiceUserImpl(userDao, myCache);

        var newAddress = new AddressDataSet(UUID.randomUUID().toString(), "some address 2");
        var newPhone = new PhoneDataSet(UUID.randomUUID().toString(), "999999999");
        var newPhone2 = new PhoneDataSet(UUID.randomUUID().toString(), "4556-5667-5674");
        var user2 = new User(0, "username2", Set.of(newPhone, newPhone2), newAddress);
        cachedDbServiceUser.saveUser(user2);
        cachedDbServiceUser.getUserWithPhonesAndAddress(user2.getId());
        cachedDbServiceUser.getUserWithPhonesAndAddress(user2.getId());

         cachedDbServiceUser.getUserWithPhonesAndAddress(user.getId());
         cachedDbServiceUser.getUserWithPhonesAndAddress(user.getId());

         cachedDbServiceUser.getUser(user.getId());
         cachedDbServiceUser.getUser(user.getId());
    }
}
