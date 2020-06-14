package ru.otus.hw09.jdbc.mapper;

import ru.otus.hw09.jdbc.mapper.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String lowerCaseClassName;
    private final Map<Boolean, List<Field>> allFieldsMap;
    private final List<Field> allFieldsList;
    private final Constructor<T> noArgsConstructor;

    @SuppressWarnings("unchecked")
    public EntityClassMetaDataImpl(Class<T> clazz) {
        lowerCaseClassName = clazz.getSimpleName().toLowerCase();
        allFieldsMap = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.partitioningBy(field -> field.isAnnotationPresent(Id.class)));
        allFieldsList = allFieldsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        noArgsConstructor = (Constructor<T>) Arrays.stream(clazz.getConstructors())
                .filter(c -> c.getParameterCount() == 0).findFirst().orElseThrow();
    }

    @Override
    public String getName() {
        return lowerCaseClassName;
    }

    @Override
    public Constructor<T> getConstructor() {
        return noArgsConstructor;
    }

    @Override
    public Field getIdField() {
        return allFieldsMap.get(true).get(0);
    }

    @Override
    public List<Field> getAllFields() {
        return Collections.unmodifiableList(allFieldsList);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsMap.get(false);
    }
}
