package ru.otus.hw07.services;

public interface CassetteService {
    void storeBanknotes(int newBanknotesCount);
    void retrieveBanknotes(int count);
    int getBanknotesCount();
}
