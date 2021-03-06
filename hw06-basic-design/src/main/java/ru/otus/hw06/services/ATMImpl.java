package ru.otus.hw06.services;

import ru.otus.hw06.domain.Banknote;
import ru.otus.hw06.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw06.domain.NotSufficientFundsException;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class ATMImpl implements ATM {
    private final MoneyStorageService moneyStorageService;

    public ATMImpl(MoneyStorageService moneyStorageService) {
        this.moneyStorageService = moneyStorageService;
    }

    @Override
    public long depositMoney(List<Banknote> moneyBundle) {
        return moneyStorageService.storeMoney(moneyBundle);
    }

    @Override
    public List<Banknote> withdrawMoney(long amount) {
        try {
            return moneyStorageService.retrieveMoney(amount);
        } catch (NoSuitableBanknotesAvailableException e) {
            e.printStackTrace();
        } catch (NotSufficientFundsException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public BigInteger getAvailableMoneyCount() {
        return moneyStorageService.getAvailableMoneyCount();
    }
}
