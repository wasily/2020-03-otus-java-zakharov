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
    public void visit(ArrayEntry arrayEntry) {
        System.out.println("ArrayEntry jsonSerializer");
        Object object = arrayEntry.getObject();
        Class ofArray = object.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                ar.add(Array.get(object, i));
            }
            System.out.println(ar);
        } else {
            for (Object o : (Object[]) object) {
                System.out.println(o);
            }
        }
    }

    @Override
    public void visit(CollectionEntry collectionEntry) {
        System.out.println("CollectionEntry json serializer");
        for (Object e : (Collection) collectionEntry.getObject()) {
            System.out.println(e);
        }
    }

    @Override
    public void visit(PrimitiveEntry primitiveEntry) {
        System.out.println("PrimitiveEntry json serializer");
        System.out.println(primitiveEntry.getObject());
    }

    @Override
    public void visit(WrappedTypeEntry wrappedTypeEntry) {
        System.out.println("ObjectEntry json serializer");
        if (wrappedTypeEntry.getObject() instanceof String) {
            System.out.println("String: " + wrappedTypeEntry.getObject());
        } else {
            System.out.println(wrappedTypeEntry.getObject());
        }
    }
}
