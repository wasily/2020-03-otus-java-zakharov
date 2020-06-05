package ru.otus.hw08.serializer.services;

import ru.otus.hw08.serializer.entries.ArrayEntry;
import ru.otus.hw08.serializer.entries.CollectionEntry;
import ru.otus.hw08.serializer.entries.WrappedTypeEntry;
import ru.otus.hw08.serializer.entries.PrimitiveEntry;

public interface Serializer {
   void visit(ArrayEntry arrayEntry);
   void visit(CollectionEntry collectionEntry);
   void visit(PrimitiveEntry primitiveEntry);
   void visit(WrappedTypeEntry wrappedTypeEntry);
}
