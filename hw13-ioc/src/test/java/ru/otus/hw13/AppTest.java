package ru.otus.hw13;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw13.appcontainer.AppComponentsContainerImpl;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.services.GameProcessor;
import ru.otus.hw13.services.GameProcessorImpl;

class AppTest {
    private AppComponentsContainer container;

    @BeforeEach
    void setUp(){
        container = new AppComponentsContainerImpl(AppConfig.class);
    }

    @Test
    void shouldGetBeanByInterface(){
        Assertions.assertNotNull(container.getAppComponent(GameProcessor.class));
    }

    @Test
    void shouldGetBeanByImplementation(){
        Assertions.assertNotNull(container.getAppComponent(GameProcessorImpl.class));
    }

    @Test
    void shouldGetBeanByName(){
        Assertions.assertNotNull(container.getAppComponent("gameProcessor"));
    }

}