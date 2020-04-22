package ru.otus.hw03;

import ru.otus.hw03.example.ClassAfterFail;
import ru.otus.hw03.example.ClassBeforeFail;
import ru.otus.hw03.example.OtherTestClass;
import ru.otus.hw03.example.TestClass;
import ru.otus.hw03.runner.TestRunner;

public class Starter {
    public static void main(String[] args) {
        new TestRunner<>(TestClass.class).runTests();
        new TestRunner<>(OtherTestClass.class).runTests();
        new TestRunner<>(ClassBeforeFail.class).runTests();
        new TestRunner<>(ClassAfterFail.class).runTests();
    }
}
