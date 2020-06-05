package ru.otus.hw08.serializer;

import java.util.Collection;

public class SampleOuterObject {
    private final int intValue;
    private final Float[] floats;
    private final Collection<String> stringCollection;
    private final SampleInnerObject sampleInnerObject;

    public SampleOuterObject(int intValue, Float[] floats, Collection<String> stringCollection, SampleInnerObject sampleInnerObject) {
        this.intValue = intValue;
        this.floats = floats;
        this.stringCollection = stringCollection;
        this.sampleInnerObject = sampleInnerObject;
    }
}
