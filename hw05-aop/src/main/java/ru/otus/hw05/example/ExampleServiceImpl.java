package ru.otus.hw05.example;

import ru.otus.hw05.annotation.Log;

import java.time.LocalDateTime;

public class ExampleServiceImpl implements ExampleService {
    @Override
    @Log
    public void sendMessage(String message, boolean isUrgent) {
        if (isUrgent) {
            System.out.println("Urgent! : " + message.toUpperCase());
            return;
        }
        System.out.println(message);
    }

    @Override
    public void sendMessage(String message, int times) {
        for (int i = 0; i < times; i++) {
            System.out.println(message);
        }
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void printCurrentTimeStamp() {
        System.out.println("ExampleService current time: " + LocalDateTime.now());
    }
}
