package ru.otus.hw08.serializer;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;
import ru.otus.hw08.serializer.services.JsonSerializer;
import ru.otus.hw08.serializer.services.Serializer;

import java.lang.reflect.Field;
import java.util.Collection;

public class MyGson {
    private final Serializer serializer = new JsonSerializer();

    public String toJson(Object object) {
        if (object == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (object.getClass().isArray()) {
            stringBuilder.append(new ArrayEntry(object).serialize(serializer));
        } else if (object instanceof Collection) {
            stringBuilder.append(new CollectionEntry(object).serialize(serializer));
        } else if (isWrappedType(object)) {
            stringBuilder.append(new WrappedTypeEntry(object).serialize(serializer));
        } else {
            stringBuilder.append("{");
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.getType().isPrimitive()) {
                        stringBuilder.append(getJsonEntry(field.getName(), new PrimitiveEntry(field.get(object)).serialize(serializer)));
                    } else if (field.getType().isArray()) {
                        stringBuilder.append(getJsonEntry(field.getName(), new ArrayEntry(field.get(object)).serialize(serializer)));
                    } else if (field.getType() == Collection.class) {
                        stringBuilder.append(getJsonEntry(field.getName(), new CollectionEntry(field.get(object)).serialize(serializer)));
                    } else if (isWrappedType(field.get(object))) {
                        stringBuilder.append(getJsonEntry(field.getName(), new WrappedTypeEntry(field.get(object)).serialize(serializer)));
                    } else {
                        stringBuilder.append(getJsonEntry(field.getName(), toJson(field.get(object))));
                    }
                    stringBuilder.append(",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
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

    private String getJsonEntry(String fieldName, String value) {
        return "\"" + fieldName + "\"" + " : " + value;
    }
}