package ru.otus.hw08.serializer;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode
public class SampleObject2 {
    @EqualsAndHashCode
    private static class SampleObject3 {
        private final String sampleObject3String = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    }

    private final String sampleObject2String = "000000000000000000000000000000";
    private final SampleObject3 innerrObject = new SampleObject3();
    private final SampleObject3[] innerrObjectArr = new SampleObject3[]{new SampleObject3(), new SampleObject3()};
    private final SampleObject3[] emptyInnerrObjectArr = new SampleObject3[0];
    private final Collection<SampleObject3> innerrObjectColl = List.of(new SampleObject3(),new SampleObject3());
    private final Collection<SampleObject3> emptyInnerrObjectColl = new ArrayList<>();
}

