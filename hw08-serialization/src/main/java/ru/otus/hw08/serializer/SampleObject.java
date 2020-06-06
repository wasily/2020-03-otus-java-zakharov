package ru.otus.hw08.serializer;

import lombok.Builder;

import java.util.Collection;

@Builder
public class SampleObject {
    private final String string;
    private final int intValue;
    private final Long aLong;
    private final char aChar;
    private final Float aFloat;
    private final long[] longArray;
    private final String[] strings;
    private final char[] chars;
    private final Double[] doublesArray;
    private final Collection<Double> doubleCollection;
    private final Collection<String> stringCollection;
    private final Collection<Character> characterCollection;
}
