package ru.otus.hw05.example.services;

import ru.otus.hw05.annotation.Log;

public class BlackholeServiceImpl implements ExampleService {
    @Override
    public void sendMessage(String message, boolean isUrgent) {

    }

    @Override
    @Log
    public void sendMessage(String message, int times) {

    }

    @Override
    @Log
    public void sendMessage(String message) {

    }

    @Override
    public void printCurrentTimeStamp() {

    }
}
