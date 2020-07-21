package ru.otus.hw13.config.separatedConfig;

import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.hw13.services.IOService;
import ru.otus.hw13.services.PlayerService;
import ru.otus.hw13.services.PlayerServiceImpl;

@AppComponentsContainerConfig(order = 1)
public class PlayerServiceConfig {
    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }
}
