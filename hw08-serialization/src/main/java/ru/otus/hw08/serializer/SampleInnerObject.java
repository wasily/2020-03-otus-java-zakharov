package ru.otus.hw08.serializer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SampleInnerObject {
    private final int intValue;
    private final Collection<String> stringCollection;

    public SampleInnerObject(int intValue, Collection<String> stringCollection) {
        this.intValue = intValue;
        this.stringCollection = stringCollection;
    }
}
