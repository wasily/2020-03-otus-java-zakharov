package ru.otus.hw07.banknotestrategy;

import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;

import java.util.EnumMap;

public class BanknotesStrategyContext {
    private BanknotesStrategy banknotesStrategy;

    public void setBanknotesStrategy(BanknotesStrategy banknotesStrategy) {
        this.banknotesStrategy = banknotesStrategy;
    }

    public EnumMap<Denomination, Integer> applyBanknotesStrategy() throws NoSuitableBanknotesAvailableException {
        return banknotesStrategy.getBanknotesSetVariant();
    }
}
