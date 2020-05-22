package ru.otus.hw07.services;

import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;

import java.util.List;

public interface ATM {
    long depositMoney(List<Banknote> moneyBundle);
    List<Banknote> withdrawMoney(StrategyEnum strategyEnum, long amount);
    long getAvailableMoneyCount();
}
