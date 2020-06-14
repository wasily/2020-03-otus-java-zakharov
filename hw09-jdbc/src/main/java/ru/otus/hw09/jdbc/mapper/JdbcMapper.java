package ru.otus.hw09.jdbc.mapper;

public interface JdbcMapper<T> {
    void insert(T objectData);

    void update(T objectData);

    void insertOrUpdate(T objectData);

    T findById(long id, Class<T> clazz);
}
