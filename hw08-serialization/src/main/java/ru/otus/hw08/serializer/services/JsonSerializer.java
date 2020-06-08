package ru.otus.hw08.serializer.services;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class JsonSerializer implements Serializer {
    @Override
    public String visit(ArrayEntry arrayEntry) {
        if (arrayEntry.getObject() == null || Array.getLength(arrayEntry.getObject()) == 0) {
            return "[]";
        }
        Object object = arrayEntry.getObject();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < Array.getLength(object); i++) {
            sb.append(visit(new WrappedTypeEntry(Array.get(object, i)))).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
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
