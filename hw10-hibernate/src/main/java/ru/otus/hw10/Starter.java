package ru.otus.hw10;

import org.hibernate.SessionFactory;
import ru.otus.hw10.core.dao.UserDao;
import ru.otus.hw10.core.model.User;
import ru.otus.hw10.core.service.DBServiceUser;
import ru.otus.hw10.core.service.DbServiceUserImpl;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.dao.UserDaoHibernate;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

public class Starter {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        long id = dbServiceUser.saveUser(new User(0, "username"));
        dbServiceUser.getUser(id);
    }
}
