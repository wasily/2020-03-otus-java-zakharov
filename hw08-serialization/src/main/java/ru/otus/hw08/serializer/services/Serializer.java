package ru.otus.hw08.serializer.services;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;

public interface Serializer {
   String visit(ArrayEntry arrayEntry);
   String visit(CollectionEntry collectionEntry);
   String visit(PrimitiveEntry primitiveEntry);
   String visit(WrappedTypeEntry wrappedTypeEntry);
}
