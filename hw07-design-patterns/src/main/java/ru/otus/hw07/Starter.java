package ru.otus.hw07;

import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.department.ATMDepartment;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.services.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Starter {
    public static void main(String[] args) {
        Map<Denomination, CassetteService> cassetteMap1 = new TreeMap<>();
        Map<Denomination, CassetteService> cassetteMap2 = new TreeMap<>();
        Map<Denomination, CassetteService> cassetteMap3 = new TreeMap<>();
        for (var den : Denomination.values()) {
            cassetteMap1.put(den, new CassetteServiceImpl(0));
            cassetteMap2.put(den, new CassetteServiceImpl(0));
            cassetteMap3.put(den, new CassetteServiceImpl(0));
        }
        ATM atm1 = new ATMImpl(new MoneyStorageServiceImpl(cassetteMap1));
        ATM atm2 = new ATMImpl(new MoneyStorageServiceImpl(cassetteMap2));
        ATM atm3 = new ATMImpl(new MoneyStorageServiceImpl(cassetteMap3));
        ATMDepartment atmDepartment = new ATMDepartment(Set.of(atm1, atm2, atm3));

        var banknoteList = List.of(
                new Banknote(Denomination.FIFTY),
                new Banknote(Denomination.FIFTY),
                new Banknote(Denomination.FIVE_HUNDRED),
                new Banknote(Denomination.FIVE_THOUSAND),
                new Banknote(Denomination.TEN),
                new Banknote(Denomination.ONE_HUNDRED),
                new Banknote(Denomination.ONE_THOUSAND),
                new Banknote(Denomination.FIVE),
                new Banknote(Denomination.TWO_THOUSAND),
                new Banknote(Denomination.TWO_HUNDRED)
        );
        System.out.println("atm1 deposit " + atm1.depositMoney(banknoteList));
        System.out.println("atm2 deposit " + atm2.depositMoney(banknoteList));
        System.out.println("atm3 deposit " + atm3.depositMoney(List.of(new Banknote(Denomination.TWO_HUNDRED))));
        System.out.println("atm3 balance " + atm3.getAvailableMoneyCount());
        System.out.println("department balance " + atmDepartment.getAvailableMoneyCount());
        System.out.println("atm2 withdraw " + atm2.withdrawMoney(StrategyEnum.REGULAR, 1_100));
        System.out.println("atm2 balance " + atm2.getAvailableMoneyCount());
        System.out.println("atm1 balance " + atm1.getAvailableMoneyCount());
        System.out.println("department balance " + atmDepartment.getAvailableMoneyCount());
    }
}
