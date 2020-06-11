package ru.otus.hw09.jdbc.mapper;

import ru.otus.hw09.jdbc.mapper.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private final Map<Boolean, List<Field>> allFieldsMap;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        allFieldsMap = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.partitioningBy(field -> field.isAnnotationPresent(Id.class)));
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        var constructors = clazz.getConstructors();
        var noArgsConstructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterCount() == 0).findFirst().get();
        return (Constructor<T>) noArgsConstructor;
    }

    @Override
    public Field getIdField() {
        return allFieldsMap.get(true).get(0);
    }

    @Override
    public List<Field> getAllFields() {
        return allFieldsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsMap.get(false);
    }
}
