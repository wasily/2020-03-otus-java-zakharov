package ru.otus.hw13.appcontainer;

public class ContextCreationException extends RuntimeException {
    public ContextCreationException(Exception e) {
        super(e);
    }

    public ContextCreationException(String message) {
        super(message);
    }
}
