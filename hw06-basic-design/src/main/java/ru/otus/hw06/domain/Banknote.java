package ru.otus.hw06.domain;

public class Banknote {
    private final Denomination denomination;

    public Banknote(Denomination denomination) {
        this.denomination = denomination;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public long getValue() {
        return denomination.getDenominationValue();
    }
}
