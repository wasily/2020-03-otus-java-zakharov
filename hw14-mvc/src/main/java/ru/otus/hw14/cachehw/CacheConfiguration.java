package ru.otus.hw14.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw14.core.model.User;

@Configuration
public class CacheConfiguration {
    private final Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

    @Bean
    public HwListener<String, User> listener() {
        return new HwListener<>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
    }
}
