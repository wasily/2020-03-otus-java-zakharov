package ru.otus.hw03.example;

import ru.otus.hw03.annotations.After;
import ru.otus.hw03.annotations.Before;
import ru.otus.hw03.annotations.Test;

public class TestClass {
    @Before
    public void before1() {

    }

    @Before
    public void before2() {

    }

    @Test
    public void test1() {
    }

    @Test
    public void test2() {
        throw new RuntimeException();
    }

    @Test
    public void test3() {
    }

    @After
    public void after1() {

    }

    @After
    public void after2() {

    }

}
