package ru.otus.hw07.domain;

public class NoSuitableBanknotesAvailableException extends Exception {
    public NoSuitableBanknotesAvailableException(String message) {
        super(message);
    }
}
