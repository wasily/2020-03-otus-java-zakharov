package ru.otus.hw07.banknotestrategy;

import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;

import java.util.EnumMap;

public interface BanknotesStrategy {
    EnumMap<Denomination, Integer> getBanknotesSetVariant() throws NoSuitableBanknotesAvailableException;
}
