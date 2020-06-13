package ru.otus.hw08.serializer;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;
import ru.otus.hw08.serializer.services.JsonSerializer;
import ru.otus.hw08.serializer.services.Serializer;

import java.lang.reflect.Array;
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
            stringBuilder.append(getSerializedArray(object));
        } else if (object instanceof Collection) {
            stringBuilder.append(getSerializedCollection((Collection) object));
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
                        stringBuilder.append(getSerializedFieldArray(field, object));
                    } else if (field.getType() == Collection.class) {
                        stringBuilder.append(getSerializedFieldCollection(field, object));
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

    private String getSerializedFieldArray(Field field, Object object) {
        try {
            if (field.get(object) == null || Array.getLength(field.get(object)) == 0) {
                return getJsonEntry(field.getName(), "[]");
            }
            return getJsonEntry(field.getName(), getSerializedArray(field.get(object)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "null";
        }

    }

    private String getSerializedArray(Object object) {
        if (Array.getLength(object) == 0) {
            return "[]";
        }
        if (!isWrappedType(Array.get(object, 0))) {
            StringBuilder s = new StringBuilder();
            s.append("[");
            for (int i = 0; i < Array.getLength(object); i++) {
                s.append(toJson(Array.get(object, i))).append(",");
            }
            s.deleteCharAt(s.length() - 1).append("]");
            return s.toString();
        }
        return new ArrayEntry(object).serialize(serializer);
    }

    private String getSerializedFieldCollection(Field field, Object object) {
        try {
            var collection = (Collection) field.get(object);
            if (field.get(object) == null || collection.size() == 0) {
                return getJsonEntry(field.getName(), "[]");
            }
            return getJsonEntry(field.getName(), getSerializedCollection(collection));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "null";
        }
    }

    private String getSerializedCollection(Collection collection) {
        if (collection.size() == 0) {
            return "[]";
        }
        if (!isWrappedType(collection.iterator().next())) {
            StringBuilder s = new StringBuilder();
            s.append("[");
            for (Object e : collection) {
                s.append(toJson(e)).append(",");
            }
            return s.deleteCharAt(s.length() - 1).append("]").toString();
        }
        return new CollectionEntry(collection).serialize(serializer);
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
        return "\"" + fieldName + "\"" + ":" + value;
    }
}