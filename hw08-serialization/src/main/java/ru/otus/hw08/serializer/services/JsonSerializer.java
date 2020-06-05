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
        StringBuilder sb = new StringBuilder();
        Object object = arrayEntry.getObject();
        Class ofArray = object.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(object);
            if (ofArray.getTypeName().equals("char")) {
                for (int i = 0; i < length; i++) {
                    ar.add("\"" + Array.get(object, i) + "\"");
                }
            } else {
                for (int i = 0; i < length; i++) {
                    ar.add(Array.get(object, i));
                }
            }
            sb.append(ar);
        } else if (ofArray.getTypeName().equals("java.lang.String") || ofArray.getTypeName().equals("java.lang.Character")) {
            List stringList = new ArrayList();
            for (Object str : (Object[]) object) {
                stringList.add("\"" + str + "\"");
            }
            sb.append(stringList);
        } else {
            List list = new ArrayList();
            for (Object obj : (Object[]) object) {
                list.add(obj);
            }
            sb.append(list);
        }
        return sb.toString();
    }

    @Override
    public String visit(CollectionEntry collectionEntry) {
        if (collectionEntry.getObject() == null) {
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
