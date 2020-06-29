package ru.otus.hw12.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final WeakHashMap<String, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(String.valueOf(key), value);
        listeners.forEach(listener -> notifyListener(listener, key, value, "cache put event"));
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(String.valueOf(key));
        listeners.forEach(listener -> notifyListener(listener, key, value, "cache remove event"));
    }

    @Override
    public V get(K key) {
        V value = cache.get(String.valueOf(key));
        listeners.forEach(listener -> notifyListener(listener, key, value, "cache get event"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public long getSize() {
        return cache.size();
    }

    private void notifyListener(HwListener<K, V> listener, K key, V value, String actionString) {
        try {
            listener.notify(key, value, actionString);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
        }
    }
}
