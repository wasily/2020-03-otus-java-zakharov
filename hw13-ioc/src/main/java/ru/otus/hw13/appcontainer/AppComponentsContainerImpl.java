package ru.otus.hw13.appcontainer;

import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        try {
            var appConfigClassInstance = configClass.getConstructor().newInstance();
            var methodsStream = Arrays.stream(configClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(AppComponent.class));
            var methodsMap = methodsStream
                    .collect(Collectors.groupingBy(method -> method.getAnnotation(AppComponent.class).order(),
                            Collectors.toList()));
            methodsMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .flatMap(entry -> entry.getValue().stream())
                    .forEach(method -> {
                        try {
                            if (method.getParameterCount() == 0) {
                                registerBean(method.invoke(appConfigClassInstance),
                                        method.getAnnotation(AppComponent.class).name());
                            } else {
                                Class<?>[] methodParameters = method.getParameterTypes();
                                List<Object> args = new ArrayList<>(methodParameters.length);
                                for (Class<?> methodParameter : methodParameters) {
                                    for (Object appComponent : appComponents) {
                                        if (methodParameter.isAssignableFrom(appComponent.getClass())) {
                                            args.add(appComponent);
                                            break;
                                        }
                                    }
                                }
                                registerBean(method.invoke(appConfigClassInstance, args.toArray()),
                                        method.getAnnotation(AppComponent.class).name());
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private void registerBean(Object bean, String name) {
        appComponents.add(bean);
        appComponentsByName.put(name, bean);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object appComponent : appComponents) {
            if (componentClass.isAssignableFrom(appComponent.getClass())) {
                return (C) appComponent;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
