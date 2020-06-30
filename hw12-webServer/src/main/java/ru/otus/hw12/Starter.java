package ru.otus.hw12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.cachehw.HwCache;
import ru.otus.hw12.cachehw.HwListener;
import ru.otus.hw12.cachehw.MyCache;
import ru.otus.hw12.core.dao.UserDao;
import ru.otus.hw12.core.model.AddressDataSet;
import ru.otus.hw12.core.model.PhoneDataSet;
import ru.otus.hw12.core.model.User;
import ru.otus.hw12.core.service.DBServiceUser;
import ru.otus.hw12.core.service.DbServiceUserImpl;
import ru.otus.hw12.helpers.FileSystemHelper;
import ru.otus.hw12.hibernate.HibernateUtils;
import ru.otus.hw12.hibernate.dao.UserDaoHibernate;
import ru.otus.hw12.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.hw12.server.UsersWebServer;
import ru.otus.hw12.server.UsersWebServerWithBasicSecurity;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.services.TemplateProcessorImpl;

public class Starter {
    private static final Logger logger = LoggerFactory.getLogger(Starter.class);
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        logger.info("Started using MyCache");
        HwCache<String, User> myCache = new MyCache<>();
        HwListener<String, User> listener = new HwListener<>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        myCache.addListener(listener);
        DBServiceUser cachedDbServiceUser = new DbServiceUserImpl(new UserDaoHibernate(sessionManager), myCache);

        cachedDbServiceUser.saveUser(new User(1L, "Крис Гир", "user1", "11111", null, null));
        cachedDbServiceUser.saveUser(new User(2L, "Ая Кэш", "user2", "11111", null, null));
        cachedDbServiceUser.saveUser(new User(3L, "Десмин Боргес", "user3", "11111", null, null));
        cachedDbServiceUser.saveUser(new User(4L, "Кетер Донохью", "user4", "11111", null, null));
        cachedDbServiceUser.saveUser(new User(5L, "Стивен Шнайдер", "user5", "11111", null, null));
        cachedDbServiceUser.saveUser(new User(6L, "Джанет Вэрни", "user6", "11111", null, null));
        cachedDbServiceUser.saveUser(new User(7L, "Брэндон Смит", "user7", "11111", null, null));

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        //LoginService loginService = new InMemoryLoginServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, cachedDbServiceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
