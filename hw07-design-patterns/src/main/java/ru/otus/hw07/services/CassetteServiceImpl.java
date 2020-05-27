package ru.otus.hw07.services;

public class CassetteServiceImpl implements CassetteService {
    private int storedBanknotesCount;

    public CassetteServiceImpl(int initialBanknotesCount) {
        if (initialBanknotesCount >= 0) {
            this.storedBanknotesCount = initialBanknotesCount;
        } else {
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + " Некорректное начальное состояние :" + initialBanknotesCount);
        }
    }

    @Override
    public void storeBanknotes(int newBanknotesCount){
        if (newBanknotesCount >= 0) {
            this.storedBanknotesCount += newBanknotesCount;
        }
    }

    @Override
    public void retrieveBanknotes(int count){
        if (count >= 0 && count <= storedBanknotesCount) {
            this.storedBanknotesCount -= count;
        }
    }

    @Override
    public int getBanknotesCount() {
        return storedBanknotesCount;
    }

    @Override
    public CassetteService copy() {
        return new CassetteServiceImpl(storedBanknotesCount);
    }
}
