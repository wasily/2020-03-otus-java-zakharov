package ru.otus.hw08.serializer.entries;

import ru.otus.hw08.serializer.services.Serializer;

public class CollectionEntry implements Entry {
    private final Object object;

    public CollectionEntry(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public void serialize(Serializer serializer) {
        serializer.visit(new CollectionEntry(object));
    }
}
