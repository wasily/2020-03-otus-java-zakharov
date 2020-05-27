package ru.otus.hw07.banknotestrategy;

import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;

import java.util.EnumMap;

public class GreedyBanknotesStrategy implements BanknotesStrategy {
    @Override
    public EnumMap<Denomination, Integer> getBanknotesSetVariant() throws NoSuitableBanknotesAvailableException {
       throw new NoSuitableBanknotesAvailableException("you can`t never get money from me");
    }
}
