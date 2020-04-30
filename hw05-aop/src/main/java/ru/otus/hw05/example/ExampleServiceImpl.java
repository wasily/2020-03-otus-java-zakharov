package ru.otus.hw05.example;

public class ExampleServiceImpl implements ExampleService {
    @Override
    public void sendMessage(String message, boolean isUrgent) {
        if (isUrgent) {
            System.out.println("Urgent! : " + message.toUpperCase());
            return;
        }
        System.out.println(message);
    }
}
