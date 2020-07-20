package ru.otus.hw14.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw14.cachehw.HwCache;
import ru.otus.hw14.cachehw.HwListener;
import ru.otus.hw14.cachehw.MyCache;
import ru.otus.hw14.core.model.User;

import java.util.List;

@Configuration
public class DBServiceUserConfiguration {
    private final Logger logger = LoggerFactory.getLogger(DBServiceUserConfiguration.class);

    private HwListener<String, User> listener() {
        return new HwListener<>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
    }

    @Bean
    public HwCache<String, User> cache() {
        return new MyCache<>(List.of(listener()));
    }
}
