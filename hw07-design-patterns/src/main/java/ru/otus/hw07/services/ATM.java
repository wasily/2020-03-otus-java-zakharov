package ru.otus.hw07.services;

import ru.otus.hw07.domain.Banknote;

import java.math.BigInteger;
import java.util.List;

public interface ATM {
    long depositMoney(List<Banknote> moneyBundle);
    List<Banknote> withdrawMoney(long amount);
    BigInteger getAvailableMoneyCount();
}
