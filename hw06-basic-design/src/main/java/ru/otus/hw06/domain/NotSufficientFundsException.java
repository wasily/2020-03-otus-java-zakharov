package ru.otus.hw06.domain;

public class NotSufficientFundsException extends Exception {
    public NotSufficientFundsException(String message) {
        super(message);
    }
}
