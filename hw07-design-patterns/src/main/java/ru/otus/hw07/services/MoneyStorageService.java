package ru.otus.hw07.services;

import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.math.BigInteger;
import java.util.List;

public interface MoneyStorageService {
    long storeMoney(List<Banknote> moneyBundle);
    List<Banknote> retrieveMoney(long amount) throws NoSuitableBanknotesAvailableException, NotSufficientFundsException;
    BigInteger getAvailableMoneyCount();
    int getAvailableBanknotesCount(Denomination banknoteDenomination);
}
