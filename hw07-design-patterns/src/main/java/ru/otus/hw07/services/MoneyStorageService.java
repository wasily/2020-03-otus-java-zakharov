package ru.otus.hw07.services;

import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.util.List;

public interface MoneyStorageService {
    long storeMoney(List<Banknote> moneyBundle);
    List<Banknote> retrieveMoney(StrategyEnum strategyEnum, long amount) throws NoSuitableBanknotesAvailableException, NotSufficientFundsException;
    long getAvailableMoneyCount();
    int getAvailableBanknotesCount(Denomination banknoteDenomination);
}
