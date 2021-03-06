package ru.otus.hw13;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw13.appcontainer.AppComponentsContainerImpl;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.config.separatedConfig.EquationPreparerConfig;
import ru.otus.hw13.config.separatedConfig.GameProcessorConfig;
import ru.otus.hw13.config.separatedConfig.IOServiceConfig;
import ru.otus.hw13.config.separatedConfig.PlayerServiceConfig;
import ru.otus.hw13.services.*;

import java.util.ArrayList;

class AppTestSeparatedConfiguration {
    private AppComponentsContainer container;

    @BeforeEach
    void setUp() {
        container = new AppComponentsContainerImpl(EquationPreparerConfig.class,
                GameProcessorConfig.class, IOServiceConfig.class, PlayerServiceConfig.class);
    }

    @Test
    void shouldFailConfiguration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AppComponentsContainerImpl(ArrayList.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AppComponentsContainerImpl(AppConfig.class, ArrayList.class));
    }

    @Test
    void shouldGetBeanByInterface() {
        Assertions.assertNotNull(container.getAppComponent(GameProcessor.class));
        Assertions.assertNotNull(container.getAppComponent(EquationPreparer.class));
        Assertions.assertNotNull(container.getAppComponent(IOService.class));
        Assertions.assertNotNull(container.getAppComponent(PlayerService.class));
    }

    @Test
    void shouldGetBeanByImplementation() {
        Assertions.assertNotNull(container.getAppComponent(GameProcessorImpl.class));
        Assertions.assertNotNull(container.getAppComponent(EquationPreparerImpl.class));
        Assertions.assertNotNull(container.getAppComponent(IOServiceConsole.class));
        Assertions.assertNotNull(container.getAppComponent(PlayerServiceImpl.class));
    }

    @Test
    void shouldGetBeanByName() {
        Assertions.assertNotNull(container.getAppComponent("gameProcessor"));
        Assertions.assertNotNull(container.getAppComponent("equationPreparer"));
        Assertions.assertNotNull(container.getAppComponent("ioService"));
        Assertions.assertNotNull(container.getAppComponent("playerService"));
    }

}