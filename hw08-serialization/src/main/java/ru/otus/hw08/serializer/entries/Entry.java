package ru.otus.hw08.serializer.entries;

import ru.otus.hw08.serializer.services.Serializer;

public interface Entry {
    String serialize(Serializer serializer);
}
