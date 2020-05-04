package ru.otus.hw05;

import ru.otus.hw05.example.context.Context;
import ru.otus.hw05.example.services.BlackholeServiceImpl;
import ru.otus.hw05.example.services.ExampleService;
import ru.otus.hw05.example.services.ConsoleServiceImpl;

public class Starter {
    public static void main(String[] args) {
        ExampleService consoleService = new ConsoleServiceImpl();
        ExampleService consoleServiceProxy = Context.getExampleService(consoleService);
        consoleServiceProxy.printCurrentTimeStamp();
        consoleServiceProxy.sendMessage("sample msg", false);
        consoleServiceProxy.sendMessage("urgent message", true);
        consoleServiceProxy.sendMessage("repeated message", 3);

        ExampleService blackholeService = new BlackholeServiceImpl();
        ExampleService blackholeServiceProxy = Context.getExampleService(blackholeService);
        blackholeServiceProxy.sendMessage("something", true);
        blackholeServiceProxy.printCurrentTimeStamp();
        blackholeServiceProxy.sendMessage("another string");
        blackholeServiceProxy.sendMessage("repeat me", 5);
    }
}
