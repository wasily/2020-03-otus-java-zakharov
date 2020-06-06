package ru.otus.hw08.serializer;

import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
public class SampleInnerObject {
    private final int intValue;
    private final Collection<String> stringCollection;

    public SampleInnerObject(int intValue, Collection<String> stringCollection) {
        this.intValue = intValue;
        this.stringCollection = stringCollection;
    }
}
