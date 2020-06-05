package ru.otus.hw08.serializer.entries;

import ru.otus.hw08.serializer.services.Serializer;

public interface Entry {
    void serialize(Serializer serializer);
}
