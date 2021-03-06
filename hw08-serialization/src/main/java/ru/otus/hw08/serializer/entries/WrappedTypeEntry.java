package ru.otus.hw08.serializer.entries;

import ru.otus.hw08.serializer.services.Serializer;

public class WrappedTypeEntry implements Entry {
    private final Object object;

    public WrappedTypeEntry(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.visit(this);
    }
}
