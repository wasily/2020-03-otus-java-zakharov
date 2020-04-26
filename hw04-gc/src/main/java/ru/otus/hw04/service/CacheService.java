package ru.otus.hw04.service;

public interface CacheService<S, U> {
    void put(S id, U element);
    U get(S id);
}
