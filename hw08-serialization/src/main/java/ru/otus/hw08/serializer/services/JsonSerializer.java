package ru.otus.hw08.serializer.services;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonSerializer implements Serializer {
    @Override
    public String visit(ArrayEntry arrayEntry) {
        if (arrayEntry.getObject() == null) {
            return "[]";
        }
        Object object = arrayEntry.getObject();
        Class ofArray = object.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(object);
            if (ofArray.arrayType() == char[].class) {
                for (int i = 0; i < length; i++) {
                    ar.add("\"" + Array.get(object, i) + "\"");
                }
            } else {
                for (int i = 0; i < length; i++) {
                    ar.add(Array.get(object, i));
                }
            }
            return ar.toString();
        } else if (ofArray.arrayType() == String[].class || ofArray.arrayType() == Character[].class) {
            List stringList = new ArrayList();
            for (Object str : (Object[]) object) {
                stringList.add("\"" + str + "\"");
            }
            return stringList.toString();
        } else {
            List list = new ArrayList();
            for (Object obj : (Object[]) object) {
                list.add(obj);
            }
            return list.toString();
        }
    }

    @Override
    public String visit(CollectionEntry collectionEntry) {
        if (collectionEntry.getObject() == null || ((Collection) collectionEntry.getObject()).size() == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        Collection res = new ArrayList();
        Collection collection = (Collection) collectionEntry.getObject();

        var elementClass = ((Collection) collectionEntry.getObject()).iterator().next().getClass();
        if (elementClass == String.class || elementClass == Character.class) {
            for (Object e : collection) {
                res.add("\"" + e + "\"");
            }
        } else {
            for (Object e : collection) {
                res.add(e);
            }
        }
        return sb.append(res).toString();
    }

    @Override
    public String visit(PrimitiveEntry primitiveEntry) {
        if (primitiveEntry.getObject().getClass() == Character.class) {
            return "\"" + primitiveEntry.getObject() + "\"";
        }
        return primitiveEntry.getObject().toString();
    }

    @Override
    public String visit(WrappedTypeEntry wrappedTypeEntry) {
        if (wrappedTypeEntry.getObject() instanceof String || wrappedTypeEntry.getObject() instanceof Character) {
            return "\"" + wrappedTypeEntry.getObject() + "\"";
        } else {
            return wrappedTypeEntry.getObject().toString();
        }
    }
}
