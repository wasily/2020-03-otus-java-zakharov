package ru.otus.hw13.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        getSortedClassesList(initialConfigClasses).forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        getSortedClassesList(packageName).forEach(this::processConfig);
    }

    private List<Class<?>> getSortedClassesList(Class<?>... classes) {
        return Arrays.stream(classes)
                .sorted(Comparator.comparing(this::getConfigClassOrder)).collect(Collectors.toList());
    }

    private List<Class<?>> getSortedClassesList(String path) {
        Reflections reflections = new Reflections(path, new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true)
                .stream().sorted(Comparator.comparing(this::getConfigClassOrder)).collect(Collectors.toList());
    }

    private Integer getConfigClassOrder(Class<?> configClass) {
        return configClass.getAnnotation(AppComponentsContainerConfig.class).order();
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        try {
            var appConfigClassInstance = configClass.getConstructor().newInstance();
            var methodsStream = Arrays.stream(configClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(AppComponent.class));
            methodsStream.sorted(Comparator.comparing(this::getBeanOrder)).forEach(method -> {
                String beanName = method.getAnnotation(AppComponent.class).name();
                Object bean = createBean(method, appConfigClassInstance, beanName);
                registerBean(bean, beanName);
            });
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ContextCreationException(e);
        }
    }

    private Integer getBeanOrder(Method method) {
        return method.getAnnotation(AppComponent.class).order();
    }

    private boolean isBeanExists(String beanName) {
        return appComponentsByName.containsKey(beanName);
    }

    private void registerBean(Object bean, String name) {
        appComponents.add(bean);
        appComponentsByName.put(name, bean);
    }

    private Object createBean(Method method, Object targetObject, String beanName) {
        if (!isBeanExists(beanName)) {
            try {
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
                return method.invoke(targetObject, args.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ContextCreationException(e);
            }
        }
        throw new ContextCreationException("Duplicate bean creation found");
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
