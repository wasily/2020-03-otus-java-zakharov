package ru.otus.hw05.example.services;

public interface ExampleService {
    void sendMessage(String message, boolean isUrgent);
    void sendMessage(String message, int times);
    void sendMessage(String message);
    void printCurrentTimeStamp();
}
