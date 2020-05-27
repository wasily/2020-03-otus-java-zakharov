package ru.otus.hw07.department;

import ru.otus.hw07.services.ATM;
import ru.otus.hw07.services.ATMMemento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

public class Originator {
    private final Deque<ATMMemento> stack = new ArrayDeque<>();

    void saveState(Set<ATM> atmSet) {
        stack.push(new ATMMemento(atmSet));
    }

    Set<ATM> restoreState() {
        return stack.pop().getATMSet();
    }
}
