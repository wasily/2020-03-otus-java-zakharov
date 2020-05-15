package ru.otus.hw06.services;

import ru.otus.hw06.domain.Banknote;

import java.util.List;

public interface CassetteService {
    boolean storeBanknotes(List<Banknote> banknoteList);
    List<Banknote> retrieveBanknotes(int count);
    int getBanknotesCount();
}
