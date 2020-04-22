package ru.otus.hw03.example;

import ru.otus.hw03.annotations.After;
import ru.otus.hw03.annotations.Before;
import ru.otus.hw03.annotations.Test;

import java.io.IOException;

public class OtherTestClass {
    @Before
    public void before(){
    }

    @Test
    public void testEx() {
        throw new RuntimeException();
    }

    @Test
    public void test() {

    }

    @Test
    public void testCheckedEx() throws IOException {
        throw new IOException();
    }

    @After
    public void after() {

    }
}
