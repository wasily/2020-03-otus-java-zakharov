package ru.otus.hw07.services;

import ru.otus.hw07.banknotestrategy.BanknotesStrategyContext;
import ru.otus.hw07.banknotestrategy.GreedyBanknotesStrategy;
import ru.otus.hw07.banknotestrategy.MinBanknoteCountStrategy;
import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.util.*;
import java.util.stream.Collectors;

public class MoneyStorageServiceImpl implements MoneyStorageService {
    private final Comparator<Denomination> denominationComparator =
            (first, second) -> Long.compare(second.getDenominationValue(), first.getDenominationValue());
    private final TreeMap<Denomination, CassetteService> cassettesMap = new TreeMap<>(denominationComparator);
    private final BanknotesStrategyContext banknotesStrategyContext = new BanknotesStrategyContext();

    public MoneyStorageServiceImpl(Map<Denomination, CassetteService> cassettesMap) {
        this.cassettesMap.putAll(cassettesMap);
    }

    @Override
    public long storeMoney(List<Banknote> moneyBundle) {
        Map<Denomination, List<Banknote>> groupedBanknotes = moneyBundle.stream()
                .collect(Collectors.groupingBy(Banknote::getDenomination));
        groupedBanknotes.forEach((k, v) -> cassettesMap.get(k).storeBanknotes(v.size()));
        return groupedBanknotes.entrySet().stream()
                .map(entry -> entry.getKey().getDenominationValue() * entry.getValue().size())
                .reduce(0L, Long::sum);
    }

    @Override
    public List<Banknote> retrieveMoney(StrategyEnum strategyEnum, long amount) throws NoSuitableBanknotesAvailableException, NotSufficientFundsException {
        if (getAvailableMoneyCount() == 0 || getAvailableMoneyCount() < amount) {
            throw new NotSufficientFundsException("Не хватает денег для выдачи " + amount);
        }
        TreeMap<Denomination, Integer> availableFunds = new TreeMap<>(denominationComparator);
        availableFunds.putAll(cassettesMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getBanknotesCount())));
        switch (strategyEnum) {
            case REGULAR:
                banknotesStrategyContext.setBanknotesStrategy(new MinBanknoteCountStrategy(amount, availableFunds));
                break;
            case GREEDY:
                banknotesStrategyContext.setBanknotesStrategy(new GreedyBanknotesStrategy());
                break;
            default:
                throw new NoSuitableBanknotesAvailableException(this.getClass().getName() + " невозможно выдать требуемую сумму");
        }

        EnumMap<Denomination, Integer> banknotesCountMap = banknotesStrategyContext.applyBanknotesStrategy();
        List<Banknote> result = new ArrayList<>();
        banknotesCountMap.forEach((denomination, count) -> {
            cassettesMap.get(denomination).retrieveBanknotes(count);
            for (int i = 0; i < count; i++) {
                result.add(new Banknote(denomination));
            }
        });
        return result;
    }

    @Override
    public long getAvailableMoneyCount() {
        return cassettesMap.entrySet().stream()
                .map(entry -> entry.getKey().getDenominationValue() * entry.getValue().getBanknotesCount())
                .reduce(0L, Long::sum);
    }

    @Override
    public int getAvailableBanknotesCount(Denomination banknoteDenomination) {
        return cassettesMap.get(banknoteDenomination).getBanknotesCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyStorageServiceImpl that = (MoneyStorageServiceImpl) o;
        return cassettesMap.equals(that.cassettesMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cassettesMap);
    }

    @Override
    public MoneyStorageService copy() {
        TreeMap<Denomination, CassetteService> cassettesMapCopy = new TreeMap<>(denominationComparator);
        cassettesMapCopy.putAll(cassettesMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().copy())));
        return new MoneyStorageServiceImpl(cassettesMapCopy);
    }
}
