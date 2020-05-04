package ru.otus.hw05;

import ru.otus.hw05.example.Context;
import ru.otus.hw05.example.ExampleService;

public class Starter {
    public static void main(String[] args) {
        ExampleService service = Context.getExampleService();
        service.printCurrentTimeStamp();
        service.sendMessage("not urgent message", false);
        service.printCurrentTimeStamp();
        service.sendMessage("urgent message", true);
        service.printCurrentTimeStamp();
        service.sendMessage("repeated message", 3);
        service.printCurrentTimeStamp();
        service.sendMessage("regular message");
    }
}
