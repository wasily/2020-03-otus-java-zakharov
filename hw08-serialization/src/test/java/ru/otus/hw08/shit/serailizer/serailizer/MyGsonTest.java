package ru.otus.hw08.shit.serailizer.serailizer;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.otus.hw08.serializer.MyGson;
import ru.otus.hw08.serializer.SampleInnerObject;
import ru.otus.hw08.serializer.SampleOuterObject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyGsonTest {
    private final Gson gson = new Gson();
    private final MyGson myGson = new MyGson();

    @Test
    void testNull() {
        String string = null;
        assertThat(gson.fromJson(myGson.toJson(string), String.class)).isEqualTo(string);
    }

    @Test
    void testArray() {
        float[] floats = new float[]{1.56784576567f, 3456345634562f, 0.3f};
        Float[] floats1 = new Float[]{1.56784576567f, 3456345634562f, 0.3f};
        assertThat(gson.fromJson(myGson.toJson(floats), float[].class)).containsExactly(floats);
        assertThat(gson.fromJson(myGson.toJson(floats1), Float[].class)).containsExactly(floats1);

        char[] chars = new char[]{'a', 's', '4'};
        Character[] chars1 = new Character[]{'a', 's', '4'};
        assertThat(gson.fromJson(myGson.toJson(chars), char[].class)).containsExactly(chars);
        assertThat(gson.fromJson(myGson.toJson(chars1), Character[].class)).containsExactly(chars1);

        boolean[] booleans = new boolean[]{true, false, false, true};
        Boolean[] booleans1 = new Boolean[]{true, false, false, true};
        assertThat(gson.fromJson(myGson.toJson(booleans), boolean[].class)).containsExactly(booleans);
        assertThat(gson.fromJson(myGson.toJson(booleans1), Boolean[].class)).containsExactly(booleans1);

        String[] strings = new String[]{"a", "s", "45678"};
        assertThat(gson.fromJson(myGson.toJson(strings), String[].class)).containsExactly(strings);
    }

    @Test
    void testPrimitive() {
        int intValue = 5;
        assertEquals(intValue, gson.fromJson(myGson.toJson(intValue), int.class));

        long longValue = 5_000_000_000L;
        assertEquals(longValue, gson.fromJson(myGson.toJson(longValue), long.class));

        double doubleValue = 0.3453455d;
        assertEquals(doubleValue, gson.fromJson(myGson.toJson(doubleValue), double.class));

        char charValue = 5;
        assertEquals(charValue, gson.fromJson(myGson.toJson(charValue), char.class));

        boolean boolValue = false;
        assertEquals(boolValue, gson.fromJson(myGson.toJson(boolValue), boolean.class));

        byte byteValue = 5;
        assertEquals(byteValue, gson.fromJson(myGson.toJson(byteValue), byte.class));
    }

    @Test
    void testCollection() {

    }

    @Test
    void testComplexObject() {
        SampleInnerObject sampleInnerObject = new SampleInnerObject(3454, List.of("345_123", "fghjmvbmn345"));
        assertThat(sampleInnerObject)
                .isEqualToComparingFieldByField(gson.fromJson(myGson.toJson(sampleInnerObject), SampleInnerObject.class));

        SampleOuterObject sampleOuterObject = new SampleOuterObject(4,
                new Float[]{2345345F, -14F}, List.of("123", "345"), sampleInnerObject);
        assertThat(sampleOuterObject).usingRecursiveComparison()
                .isEqualTo(gson.fromJson(myGson.toJson(sampleOuterObject), SampleOuterObject.class));
    }
}