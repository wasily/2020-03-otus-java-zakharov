package ru.otus.hw08;

import ru.otus.hw08.serializer.MyGson;
import ru.otus.hw08.serializer.SampleInnerObject;
import ru.otus.hw08.serializer.SampleObject;
import ru.otus.hw08.serializer.SampleOuterObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Starter {
    public static void main(String[] args) {
        MyGson myGson = new MyGson();
        System.out.println("---complex object---");
        SampleObject sampleObject = SampleObject.builder().aChar('a').aFloat(0.000000000345f).aLong(5_000_000_000L).intValue(234356)
                .string("Test string").chars(new char[]{'q', 'y'}).longArray(new long[]{32345L, 345634565L})
                .strings(new String[]{"sdfgsfd", "@#$%^&*4564"}).doublesArray(new Double[]{0.00032453456, 65465476476547.6476})
                .stringCollection(List.of("!111", "435%$%^$")).characterCollection(List.of('g', 'y')).doubleCollection(List.of(2345.2345, 6465.98709870789)).build();
        System.out.println(myGson.toJson(sampleObject));

        System.out.println("---primitive---");
        System.out.println(myGson.toJson((short) 1));
        System.out.println(myGson.toJson(7.1D));
        System.out.println(myGson.toJson('c'));
        System.out.println(myGson.toJson(true));
        System.out.println(myGson.toJson(null));

        System.out.println("---collection---");
        System.out.println(myGson.toJson(List.of("asdfas", "34534534")));
        var doubleArrayList = new ArrayList<Double>();
        doubleArrayList.add(0.023423);
        doubleArrayList.add(3456345643560.023423);
        doubleArrayList.add(3.043523423);
        System.out.println(myGson.toJson(doubleArrayList));
        var set = new HashSet<Character>();
        set.add('c');
        set.add('a');
        set.add('9');
        System.out.println(myGson.toJson(set));
        System.out.println(myGson.toJson(List.of(1, 4, 665)));
        System.out.println(myGson.toJson(Collections.EMPTY_LIST));

        System.out.println("---array---");
        System.out.println(myGson.toJson(new Float[]{100000.56784576567f, 3456345634562f, 0.00000003f}));
        System.out.println(myGson.toJson(new Boolean[]{true, false, false, true}));
        System.out.println(myGson.toJson(new String[]{"a", "s", "45678"}));
        System.out.println(myGson.toJson(new String[]{}));
        System.out.println(myGson.toJson(new char[]{'a', 's', '4'}));
        System.out.println(myGson.toJson(new Character[]{'s', 'b', '5'}));
        System.out.println(myGson.toJson(new Character[]{}));
        System.out.println(myGson.toJson(new int[]{}));

        System.out.println("---string---");
        System.out.println(myGson.toJson("asdfasd"));
        System.out.println(myGson.toJson("a�sd\nfdg\"sdrfd \\fg dfg\tfasd"));

        System.out.println("---complex object---");
        SampleInnerObject sampleInnerObject = new SampleInnerObject(3454, List.of("345_123", "fghjmvbmn345"));
        SampleOuterObject sampleOuterObject = new SampleOuterObject(4, new Float[]{1F, 0.0000005F},
                List.of("123", "345"), sampleInnerObject);
        System.out.println(myGson.toJson(sampleOuterObject));
    }
}
