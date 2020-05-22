package ru.otus.hw07.banknotestrategy;

import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;

import java.util.EnumMap;
import java.util.TreeMap;

public class MinBanknoteCountStrategy implements BanknotesStrategy {
    final long target;
    final TreeMap<Denomination, Integer> availableFunds;

    public MinBanknoteCountStrategy(long target, TreeMap<Denomination, Integer> availableFunds) {
        this.target = target;
        this.availableFunds = availableFunds;
    }

    @Override
    public EnumMap<Denomination, Integer> getBanknotesSetVariant()
            throws NoSuitableBanknotesAvailableException {
        EnumMap<Denomination, Integer> result = new EnumMap<>(Denomination.class);
        long remains = target;
        for (var denomination : availableFunds.keySet()) {
            int neededBanknotesCount = (int) (remains / denomination.getDenominationValue());
            int availableBanknotesCount = availableFunds.get(denomination);
            if (neededBanknotesCount == 0) {
                continue;
            }
            if (neededBanknotesCount > availableBanknotesCount) {
                result.put(denomination, availableBanknotesCount);
                remains -= denomination.getDenominationValue() * availableBanknotesCount;
                continue;
            }
            result.put(denomination, neededBanknotesCount);
            remains -= denomination.getDenominationValue() * neededBanknotesCount;
            if (remains == 0) {
                break;
            }
        }
        if (remains != 0) {
            throw new NoSuitableBanknotesAvailableException("Невозможно собрать требуемую сумму");
        }
        return result;
    }
}
