package ru.otus.hw08;


import ru.otus.hw08.serializer.MyGson;
import ru.otus.hw08.serializer.SampleObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Starter {
    public static void main(String[] args) {
        MyGson myGson = new MyGson();
        System.out.println("---complex object---");
        SampleObject sampleObject = new SampleObject("stringval", 123,
                Float.valueOf(6758767), new long[]{2345423L, 23457777L}, new String[]{"3543","@#$%^&"},
                List.of(0.1234, 12341.24123));
        myGson.toJson(sampleObject);

        System.out.println("---primitive---");
        myGson.toJson((short)1);
        myGson.toJson(5);
        myGson.toJson(5L);
        myGson.toJson(6.0);
        myGson.toJson(7.1D);
        myGson.toJson('c');
        myGson.toJson(true);
        myGson.toJson(Byte.valueOf("45"));

        System.out.println("---collection---");
        myGson.toJson(List.of("asdfas","34534534"));
        myGson.toJson(List.of(1,2,3));
        var arrlist = new ArrayList<Double>();
        arrlist.add(0.023423);
        arrlist.add(3456345643560.023423);
        arrlist.add(3.043523423);
        myGson.toJson(arrlist);
        var set = new HashSet<Character>();
        set.add('c');
        set.add('a');
        set.add('9');
        myGson.toJson(set);

        System.out.println("---array---");
        myGson.toJson(new int[]{1,2,3});
        myGson.toJson(new float[]{1.56784576567f,3456345634562f,0.3f});
        myGson.toJson(new Float[]{100000.56784576567f,3456345634562f,0.00000003f});
        myGson.toJson(new Boolean[]{true,false,false,true});
        myGson.toJson(new String[]{"a","s","45678"});

        System.out.println("---string---");
        myGson.toJson("asdfasd");
    }
}
