package ru.otus.hw06.services;

import ru.otus.hw06.domain.Banknote;

import java.math.BigInteger;
import java.util.List;

public interface ATM {
    long depositMoney(List<Banknote> moneyBundle);
    List<Banknote> withdrawMoney(long amount);
    BigInteger getAvailableMoneyCount();
}
