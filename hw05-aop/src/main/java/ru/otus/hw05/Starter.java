package ru.otus.hw05;

import ru.otus.hw05.example.ExampleService;
import ru.otus.hw05.example.ExampleServiceImpl;

public class Starter {
    public static void main(String[] args) {
        ExampleService service = new ExampleServiceImpl();
        service.sendMessage("test", false);
        service.sendMessage("test", true);
    }
}
