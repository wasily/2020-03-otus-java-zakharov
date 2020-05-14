package ru.otus.hw06.domain;

public class NoSuitableBanknotesAvailableException extends Exception {
    public NoSuitableBanknotesAvailableException(String message) {
        super(message);
    }
}
