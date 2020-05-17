package ru.otus.hw06.services;

import ru.otus.hw06.domain.Banknote;
import ru.otus.hw06.domain.Denomination;
import ru.otus.hw06.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw06.domain.NotSufficientFundsException;

import java.math.BigInteger;
import java.util.List;

public interface MoneyStorageService {
    long storeMoney(List<Banknote> moneyBundle);
    List<Banknote> retrieveMoney(long amount) throws NoSuitableBanknotesAvailableException, NotSufficientFundsException;
    BigInteger getAvailableMoneyCount();
    int getAvailableBanknotesCount(Denomination banknoteDenomination);
}
