package ru.otus.hw09.jdbc.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntitySQLMetaDataImplTest {
    private EntitySQLMetaData userSQLMetaData;
    private EntitySQLMetaData accountSQLMetaData;

    @BeforeEach
    void setUp() {
        userSQLMetaData = new EntitySQLMetaDataImpl(new EntityClassMetaDataImpl<>(User.class));
        accountSQLMetaData = new EntitySQLMetaDataImpl(new EntityClassMetaDataImpl<>(Account.class));
    }

    @Test
    void getSelectAllSql() {
        var userSql = "select * from user";
        var accSql = "select * from account";
        assertEquals(userSql, userSQLMetaData.getSelectAllSql());
        assertEquals(accSql, accountSQLMetaData.getSelectAllSql());
    }

    @Test
    void getSelectByIdSql() {
        var userSql = "select id,name from user where id = ?";
        var accSql = "select no,rest,type from account where no = ?";
        assertEquals(userSql, userSQLMetaData.getSelectByIdSql());
        assertEquals(accSql, accountSQLMetaData.getSelectByIdSql());
    }

    @Test
    void getInsertSql() {
        var userSql = "insert into user(name) values (?)";
        var accSql = "insert into account(rest,type) values (?,?)";
        assertEquals(userSql, userSQLMetaData.getInsertSql());
        assertEquals(accSql, accountSQLMetaData.getInsertSql());
    }

    @Test
    void getUpdateSql(){
        var userSql = "update user set name = ? where id = ?";
        var accSql = "update account set rest = ?,type = ? where no = ?";
        assertEquals(userSql, userSQLMetaData.getUpdateSql());
        assertEquals(accSql, accountSQLMetaData.getUpdateSql());
    }

}