package ru.otus.hw09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.service.DbServiceAccountImpl;
import ru.otus.hw09.core.service.DbServiceUserImpl;
import ru.otus.hw09.h2.DataSourceH2;
import ru.otus.hw09.jdbc.DbExecutorImpl;
import ru.otus.hw09.jdbc.dao.MappedAccountDaoJdbc;
import ru.otus.hw09.jdbc.dao.MappedUserDaoJdbc;
import ru.otus.hw09.jdbc.dao.UserDaoJdbc;
import ru.otus.hw09.jdbc.mapper.JdbcMapperImpl;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        var dataSource = new DataSourceH2();
        var demo = new DbServiceDemo();

        demo.createTable(dataSource);

        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);

        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userDao = new UserDaoJdbc(sessionManager, dbExecutor);
        var dbServiceUser = new DbServiceUserImpl(userDao);

        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser"));
        Optional<User> user = dbServiceUser.getUser(id);
        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
        user.get().setName("new name1");
        dbServiceUser.saveUser(user.get());
        dbServiceUser.getUser(id);

        var mappedUserDaoJdbc = new MappedUserDaoJdbc(sessionManager, new JdbcMapperImpl<>(User.class, dbExecutor, sessionManager));
        var mappedDbServiceUser = new DbServiceUserImpl(mappedUserDaoJdbc);

        var user2 = new User(1234, "name2");
        var user3 = new User(12344, "name3");
        mappedDbServiceUser.saveUser(user2);
        mappedDbServiceUser.saveUser(user3);
        mappedDbServiceUser.getUser(user2.getId());
        mappedDbServiceUser.getUser(user3.getId());
        user2.setName("new Name");
        mappedDbServiceUser.saveUser(user2);
        mappedDbServiceUser.getUser(user2.getId());
        mappedDbServiceUser.getUser(user3.getId());

        DbExecutorImpl<Account> accountDbExecutor = new DbExecutorImpl<>();
        var mappedAccountDaoJdbc = new MappedAccountDaoJdbc(sessionManager, new JdbcMapperImpl<>(Account.class, accountDbExecutor, sessionManager));
        var mappedDBServiceAccount = new DbServiceAccountImpl(mappedAccountDaoJdbc);

        var account = new Account(123, "accountType", new BigDecimal("123.3465"));
        var account2 = new Account(1234, "accountType2", new BigDecimal("1000123.3465"));
        var accId = mappedDBServiceAccount.saveAccount(account);
        var accId2 = mappedDBServiceAccount.saveAccount(account2);

        mappedDBServiceAccount.getAccount(accId);
        mappedDBServiceAccount.getAccount(accId2);

        account.setRest(account.getRest().add(BigDecimal.valueOf(1_000)));
        account.setType("new type");
        mappedDBServiceAccount.saveAccount(account);
        mappedDBServiceAccount.getAccount(accId);
    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50))")) {
            pst.executeUpdate();
        }
        System.out.println("user table created");
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table account(no bigint(20) NOT NULL auto_increment, " +
                     "type varchar(255),rest number)")) {
            pst.executeUpdate();
        }
        System.out.println("account table created");
    }
}
