package example;

import annotations.After;
import annotations.Before;
import annotations.Test;

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
