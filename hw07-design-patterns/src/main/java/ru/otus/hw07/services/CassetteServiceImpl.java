package ru.otus.hw07.services;

import ru.otus.hw07.domain.Banknote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CassetteServiceImpl implements CassetteService {
    private final List<Banknote> funds;

    public CassetteServiceImpl() {
        this.funds = new ArrayList<>();
    }

    @Override
    public boolean storeBanknotes(List<Banknote> banknoteList) {
        return funds.addAll(banknoteList);
    }

    @Override
    public List<Banknote> retrieveBanknotes(int count) {
        if (count > funds.size() || count < 1) {
            return Collections.emptyList();
        }
        List<Banknote> result = new ArrayList<>(count);
        result.addAll(funds.subList(funds.size() - count, funds.size()));
        for (int i = 0; i < count; i++) {
            funds.remove(funds.size() - 1);
        }
        return result;
    }

    @Override
    public int getBanknotesCount() {
        return funds.size();
    }
}
