package ru.otus.hw07.services;

import java.util.Set;
import java.util.stream.Collectors;

public class ATMMemento {
    private final Set<ATM> atmSet;

    public ATMMemento(Set<ATM> atms) {
        this.atmSet = atms.stream()
                .map(ATM::copy)
                .collect(Collectors.toSet());
    }

    public Set<ATM> getATMSet() {
        return atmSet;
    }
}
