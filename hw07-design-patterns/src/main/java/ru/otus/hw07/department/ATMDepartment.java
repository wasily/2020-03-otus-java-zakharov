package ru.otus.hw07.department;

import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.services.ATM;

import java.util.List;
import java.util.Set;

public class ATMDepartment implements ATM {
    private Set<ATM> atmSet;
    private final Originator originator = new Originator();

    public ATMDepartment(Set<ATM> atmSet) {
        this.atmSet = atmSet;
    }

    @Override
    public long depositMoney(List<Banknote> moneyBundle) {
        throw new UnsupportedOperationException("Illegal operation on department");
    }

    @Override
    public List<Banknote> withdrawMoney(StrategyEnum strategyEnum, long amount) {
        throw new UnsupportedOperationException("Illegal operation on department");
    }

    @Override
    public long getAvailableMoneyCount() {
        return atmSet.stream().map(ATM::getAvailableMoneyCount).reduce(0L, Long::sum);
    }

    public boolean addATM(ATM atm) {
        return atmSet.add(atm);
    }

    public boolean removeATM(ATM atm) {
        return atmSet.remove(atm);
    }

    public void resetATMs() {
        this.atmSet = originator.restoreState();
    }

    public void saveATMs() {
        originator.saveState(this.atmSet);
    }

    public int getATMCount() {
        return atmSet.size();
    }

    @Override
    public ATM copy() {
        throw new UnsupportedOperationException("Illegal operation on department");
    }
}
