package hw16.core.service;

import hw16.cachehw.HwCache;
import hw16.cachehw.HwListener;
import hw16.cachehw.MyCache;
import hw16.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
