package ru.otus.hw08.serializer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SampleObject {
    private final String string;
    private final int intValue;
    private final char aChar;
    private final Float aFloat;
    private final long[] longArray;
    private final String[] strings;
    private final char[] chars;
    private final Collection<Double> doubleCollection;
    private final Collection<String> stringCollection;
    private final Collection<Character> characterCollectionCollection;


    public SampleObject(String string, int intValue, char aChar, Float aFloat, long[] longArray, String[] strings, char[] chars, List<Double> doubleCollection, Set<String> stringCollection, Collection<Character> characterCollectionCollection) {
        this.string = string;
        this.intValue = intValue;
        this.aChar = aChar;
        this.aFloat = aFloat;
        this.longArray = longArray;
        this.strings = strings;
        this.chars = chars;
        this.doubleCollection = doubleCollection;
        this.stringCollection = stringCollection;
        this.characterCollectionCollection = characterCollectionCollection;
    }
}
