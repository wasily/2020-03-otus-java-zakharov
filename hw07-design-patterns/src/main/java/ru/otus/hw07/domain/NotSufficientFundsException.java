package ru.otus.hw07.domain;

public class NotSufficientFundsException extends Exception {
    public NotSufficientFundsException(String message) {
        super(message);
    }
}
