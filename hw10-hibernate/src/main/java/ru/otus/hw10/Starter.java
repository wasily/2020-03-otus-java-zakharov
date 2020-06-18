package ru.otus.hw10;

import org.hibernate.SessionFactory;
import ru.otus.hw10.core.dao.UserDao;
import ru.otus.hw10.core.model.AddressDataSet;
import ru.otus.hw10.core.model.PhoneDataSet;
import ru.otus.hw10.core.model.User;
import ru.otus.hw10.core.service.DBServiceUser;
import ru.otus.hw10.core.service.DbServiceUserImpl;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.dao.UserDaoHibernate;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Set;
import java.util.UUID;

public class Starter {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        var address = new AddressDataSet(UUID.randomUUID().toString(),"some address");
        var phone = new PhoneDataSet(UUID.randomUUID().toString(), "1234567890");
        var phone2 = new PhoneDataSet(UUID.randomUUID().toString(), "123-1234-34");
        User user = new User(0, "username", Set.of(phone,phone2),address);
        long id = dbServiceUser.saveUser(user);
        dbServiceUser.getUser(id);
        dbServiceUser.getUserWithPhonesAndAddress(id);
    }
}
