package ru.otus.hw08.serializer;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;
import ru.otus.hw08.serializer.services.JsonSerializer;
import ru.otus.hw08.serializer.services.Serializer;

import java.lang.reflect.Field;
import java.util.Collection;

public class MyGson {
    private final Serializer serializer = new JsonSerializer();

    public String toJson(Object object) {
        if (object.getClass().isArray()) {
            new ArrayEntry(object).serialize(serializer);
        } else if (object instanceof Collection) {
            new CollectionEntry(object).serialize(serializer);
        } else if (isWrappedType(object)) {
            new WrappedTypeEntry(object).serialize(serializer);
        } else {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.getType().isPrimitive()) {
                        System.out.println("field name: " + field.getName());
                        new PrimitiveEntry(field.get(object)).serialize(serializer);
                    } else if (field.getType().isArray()) {
                        System.out.println("field name: " + field.getName());
                        new ArrayEntry(field.get(object)).serialize(serializer);
                    } else if (field.getType().isInstance(Collection.class)) {
                        System.out.println("field name: " + field.getName());
                        new CollectionEntry(field.get(object)).serialize(serializer);
                    } else if (isWrappedType(field.get(object))) {
                        System.out.println("field name: " + field.getName());
                        new WrappedTypeEntry(field.get(object)).serialize(serializer);
                    } else {
                        toJson(field.get(object));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "sdfsd";
    }

    private boolean isWrappedType(Object object) {
        return object instanceof Short ||
                object instanceof Integer ||
                object instanceof Long ||
                object instanceof Float ||
                object instanceof Double ||
                object instanceof Byte ||
                object instanceof Character ||
                object instanceof Boolean ||
                object instanceof String;
    }
}