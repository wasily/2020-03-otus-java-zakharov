package ru.otus.hw09.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.DbServiceDemo;
import ru.otus.hw09.jdbc.DbExecutor;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.stream.Collectors;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final DbExecutor<T> dbExecutor;
    private final SessionManagerJdbc sessionManager;

    public JdbcMapperImpl(Class<T> clazz, DbExecutor<T> dbExecutor, SessionManagerJdbc sessionManager) {
        this.entityClassMetaData = new EntityClassMetaDataImpl<>(clazz);
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
        this.dbExecutor = dbExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void insert(T objectData) {
        long id;
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsMap = fieldsWithoutId.stream().collect(Collectors.toMap(field -> field.getName(), field -> {
            try {
                field.setAccessible(true);
                return field.get(objectData);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }));
        var argsList = fieldsMap.keySet().stream().sorted(Comparator.naturalOrder()).map(fieldsMap::get).collect(Collectors.toList());
        try {
            id = dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(),
                    entitySQLMetaData.getInsertSql(), argsList);
            setIdField(objectData, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setIdField(T objectData, long id) {
        try {
            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            idField.setLong(objectData, id);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private long getId(T objectData) {
        var idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        try {
            return (long) idField.get(objectData);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public void update(T objectData) {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsMap = fieldsWithoutId.stream().collect(Collectors.toMap(field -> field.getName(), field -> {
            try {
                field.setAccessible(true);
                return field.get(objectData);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }));
        var argsList = fieldsMap.keySet().stream().sorted(Comparator.naturalOrder()).map(fieldsMap::get).collect(Collectors.toList());
        try {
            dbExecutor.executeUpdate(getId(objectData), sessionManager.getCurrentSession().getConnection(),
                    entitySQLMetaData.getUpdateSql(), argsList);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public void insertOrUpdate(T objectData) {
        if (findById(getId(objectData), (Class<T>) objectData.getClass()) == null) {
            insert(objectData);
        } else {
            update(objectData);
        }
    }

    @Override
    public T findById(long id, Class<T> clazz) {
        try {
            var optionalObject = dbExecutor.executeSelect(sessionManager.getCurrentSession().getConnection(),
                    entitySQLMetaData.getSelectByIdSql(), id, rs -> newObjectInstance(rs, clazz));
            return optionalObject.isEmpty() ? null : optionalObject.get();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private T newObjectInstance(ResultSet resultSet, Class<?> clazz) {
        try {
            if (resultSet.next()) {
                var object = entityClassMetaData.getConstructor().newInstance();
                for (Field field : entityClassMetaData.getAllFields()) {
                    var fieldName = field.getName();
                    var objectField = clazz.getDeclaredField(field.getName());
                    objectField.setAccessible(true);
                    objectField.set(object, resultSet.getObject(fieldName));
                }
                return (T) object;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
