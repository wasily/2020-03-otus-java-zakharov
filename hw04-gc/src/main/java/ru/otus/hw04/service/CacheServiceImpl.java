package ru.otus.hw04.service;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheServiceImpl<S, U> implements CacheService<S, U> {
    private final LinkedHashMap<S, U> cache;

    public CacheServiceImpl(int size) {
        this.cache = new LinkedHashMap<>(size, 0.8f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<S, U> eldest) {
                return size() > size;
            }
        };
    }

    @Override
    public void put(S id, U element) {
        cache.put(id, element);
    }

    @Override
    public U get(S id) {
        return cache.get(id);
    }
}
