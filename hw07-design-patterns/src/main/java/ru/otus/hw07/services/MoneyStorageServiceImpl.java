package ru.otus.hw07.services;

import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.util.*;
import java.util.stream.Collectors;

public class MoneyStorageServiceImpl implements MoneyStorageService {
    private final TreeMap<Denomination, CassetteService> cassettesMap;

    public MoneyStorageServiceImpl(TreeMap<Denomination, CassetteService> cassettesMap) {
        this.cassettesMap = cassettesMap;
    }

    @Override
    public long storeMoney(List<Banknote> moneyBundle) {
        Map<Denomination, List<Banknote>> groupedBanknotes = moneyBundle.stream()
                .collect(Collectors.groupingBy(Banknote::getDenomination));
        groupedBanknotes.forEach((k, v) -> cassettesMap.get(k).storeBanknotes(v));
        return groupedBanknotes.entrySet().stream()
                .map(entry -> entry.getKey().getDenominationValue() * entry.getValue().size())
                .reduce(0L, Long::sum);
    }

    @Override
    public List<Banknote> retrieveMoney(long amount) throws NoSuitableBanknotesAvailableException, NotSufficientFundsException {
        if (getAvailableMoneyCount() == 0 || getAvailableMoneyCount() < amount) {
            throw new NotSufficientFundsException("Не хватает денег для выдачи " + amount);
        }

        EnumMap<Denomination, Integer> banknotesCountBuffer = new EnumMap<>(Denomination.class);
        long remains = amount;
        for (var denomination : cassettesMap.keySet()) {
            int neededBanknotesCount = (int) (remains / denomination.getDenominationValue());
            int availableBanknotesCount = getAvailableBanknotesCount(denomination);
            if (neededBanknotesCount == 0) {
                continue;
            }
            if (neededBanknotesCount > availableBanknotesCount) {
                banknotesCountBuffer.put(denomination, availableBanknotesCount);
                remains -= denomination.getDenominationValue() * availableBanknotesCount;
                continue;
            }
            banknotesCountBuffer.put(denomination, neededBanknotesCount);
            remains -= denomination.getDenominationValue() * neededBanknotesCount;
            if (remains == 0) {
                break;
            }
        }
        if (remains != 0) {
            throw new NoSuitableBanknotesAvailableException("Невозможно собрать требуемую сумму");
        }
        List<Banknote> result = new ArrayList<>();
        for (var denomination : banknotesCountBuffer.keySet()) {
            int banknotesCount = banknotesCountBuffer.get(denomination);
            result.addAll(cassettesMap.get(denomination).retrieveBanknotes(banknotesCount));
        }
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
}
