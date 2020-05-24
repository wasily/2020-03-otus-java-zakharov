package ru.otus.hw07.services;

public interface CassetteService extends CassettePrototype {
    void storeBanknotes(int newBanknotesCount);
    void retrieveBanknotes(int count);
    int getBanknotesCount();
}
