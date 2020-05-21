package ru.otus.hw07.services;

import ru.otus.hw07.domain.Banknote;

import java.util.List;

public interface CassetteService {
    boolean storeBanknotes(List<Banknote> banknoteList);
    List<Banknote> retrieveBanknotes(int count);
    int getBanknotesCount();
}
