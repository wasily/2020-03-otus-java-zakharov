package ru.otus.hw03.example;

import ru.otus.hw03.annotations.After;
import ru.otus.hw03.annotations.Before;
import ru.otus.hw03.annotations.Test;

public class ClassBeforeFail {
    @Before
    public void before() {
        throw new RuntimeException();
    }

    @Test
    public void test() {

    }

    @After
    public void after() {

    }
}
