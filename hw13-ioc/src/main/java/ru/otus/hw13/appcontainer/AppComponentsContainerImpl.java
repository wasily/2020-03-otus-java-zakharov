package ru.otus.hw13.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
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

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        for (Class<?> initialConfigClass : initialConfigClasses) {
            checkConfigClass(initialConfigClass);
        }

        var configClassesMap = Arrays.stream(initialConfigClasses)
                .collect(Collectors.groupingBy(aClass -> aClass.getAnnotation(AppComponentsContainerConfig.class).order(),
                        Collectors.toList()));

        configClassesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> entry.getValue().stream())
                .forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner());
        var configClassesMap = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true).stream()
                .collect(Collectors.groupingBy(aClass -> aClass.getAnnotation(AppComponentsContainerConfig.class).order(),
                        Collectors.toList()));

        configClassesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> entry.getValue().stream())
                .forEach(this::processConfig);
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
                        String beanName = method.getAnnotation(AppComponent.class).name();
                        if (!isBeanExists(beanName)) {
                            try {
                                if (method.getParameterCount() == 0) {
                                    registerBean(method.invoke(appConfigClassInstance), beanName);
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
                                    registerBean(method.invoke(appConfigClassInstance, args.toArray()), beanName);
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private boolean isBeanExists(String beanName) {
        return appComponentsByName.containsKey(beanName);
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
