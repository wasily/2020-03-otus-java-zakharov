package ru.otus.hw07.services;

import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class MoneyStorageServiceImpl implements MoneyStorageService {
    private final EnumMap<Denomination, CassetteService> cassettesMap;

    public MoneyStorageServiceImpl(EnumMap<Denomination, CassetteService> cassettesMap) {
        this.cassettesMap = cassettesMap;
    }

    @Override
    public long storeMoney(List<Banknote> moneyBundle) {
        long storedMoney = 0;
        EnumMap<Denomination, List<Banknote>> buffer = getBanknotesEmptyBuffer();
        for (var banknote : moneyBundle) {
            buffer.get(banknote.getDenomination()).add(banknote);
            storedMoney += banknote.getDenomination().getDenominationValue();
        }
        for (var banknoteType : buffer.keySet()) {
            cassettesMap.get(banknoteType).storeBanknotes(buffer.get(banknoteType));
        }
        return storedMoney;
    }

    @Override
    public List<Banknote> retrieveMoney(long amount) throws NoSuitableBanknotesAvailableException, NotSufficientFundsException {
        if (getAvailableMoneyCount().equals(BigInteger.ZERO)
                || (getAvailableMoneyCount().compareTo(BigInteger.valueOf(amount)) == -1)) {
            throw new NotSufficientFundsException("Не хватает денег для выдачи " + amount);
        }
        List<Denomination> sorted = Arrays.asList(Denomination.values());
        sorted.sort((a, b) -> Long.compare(b.getDenominationValue(), a.getDenominationValue()));

        EnumMap<Denomination, Integer> banknotesCountBuffer = new EnumMap<>(Denomination.class);
        long remains = amount;
        for (var denomination : sorted) {
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

    private EnumMap<Denomination, List<Banknote>> getBanknotesEmptyBuffer() {
        EnumMap<Denomination, List<Banknote>> buffer = new EnumMap<>(Denomination.class);
        for (var denomination : Denomination.values()) {
            buffer.put(denomination, new ArrayList<>());
        }
        return buffer;
    }

    @Override
    public BigInteger getAvailableMoneyCount() {
        return cassettesMap.entrySet().stream()
                .map(entry -> BigInteger.valueOf(entry.getKey().getDenominationValue() * entry.getValue().getBanknotesCount()))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    @Override
    public int getAvailableBanknotesCount(Denomination banknoteDenomination) {
        return cassettesMap.get(banknoteDenomination).getBanknotesCount();
    }
}
