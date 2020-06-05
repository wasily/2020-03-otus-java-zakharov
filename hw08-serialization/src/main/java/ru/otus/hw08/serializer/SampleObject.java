package ru.otus.hw08.serializer;

import java.util.List;

public class SampleObject {
    private final String string;
    private final int intValue;
    private final Float aFloat;
    private final long[] longArray;
    private final String[] strings;
    private final List<Double> doubleList;

    public SampleObject(String string, int intValue, Float aFloat, long[] longArray, String[] strings, List<Double> doubleList) {
        this.string = string;
        this.intValue = intValue;
        this.aFloat = aFloat;
        this.longArray = longArray;
        this.strings = strings;
        this.doubleList = doubleList;
    }
}
