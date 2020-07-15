package ru.otus.hw14.hibernate;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw14.core.model.User;

@Configuration
public class HibernateConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HibernateConfiguration.class);

    @Bean
    public SessionFactory sessionFactory() {
        var sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class);
        migration();
        return sessionFactory;
    }

    private static void migration() {
        logger.info("flyway migration started");
        var flyway = Flyway.configure().dataSource("jdbc:h2:mem:DB", "sa", "sa")
                .locations("classpath:db/migration ").load();
        flyway.baseline();
        flyway.migrate();
        logger.info("flyway migration completed");
    }
}
