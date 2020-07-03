package ru.otus.hw13.config.separatedConfig;

import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.hw13.services.EquationPreparer;
import ru.otus.hw13.services.EquationPreparerImpl;

@AppComponentsContainerConfig(order = 0)
public class EquationPreparerConfig {
    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }
}
