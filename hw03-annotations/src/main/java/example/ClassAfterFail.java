package example;

import annotations.After;
import annotations.Before;
import annotations.Test;

import java.io.IOException;

public class ClassAfterFail {
    @Before
    public void before() {

    }

    @Test
    public void test1() {

    }
    @Test
    public void test2() {

    }

    @After
    public void after1() throws IOException {
        throw new IOException();
    }

    @After
    public void after2() {

    }
}
