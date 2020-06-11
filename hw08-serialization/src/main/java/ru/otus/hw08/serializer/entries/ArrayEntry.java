package ru.otus.hw08.serializer.entries;

import ru.otus.hw08.serializer.services.Serializer;

public class ArrayEntry implements Entry {
    private final Object object;

    public ArrayEntry(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.visit(new ArrayEntry(object));
    }
}
