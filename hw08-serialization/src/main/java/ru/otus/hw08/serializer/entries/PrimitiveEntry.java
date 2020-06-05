package ru.otus.hw08.serializer.entries;

import ru.otus.hw08.serializer.services.Serializer;

public class PrimitiveEntry implements Entry {
    private final Object object;

    public PrimitiveEntry(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public void serialize(Serializer serializer) {
        serializer.visit(this);
    }
}
