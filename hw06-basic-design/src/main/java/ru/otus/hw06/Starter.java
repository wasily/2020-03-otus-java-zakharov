package ru.otus.hw06;

import ru.otus.hw06.domain.Banknote;
import ru.otus.hw06.domain.Denomination;
import ru.otus.hw06.services.ATMImpl;
import ru.otus.hw06.services.CassetteService;
import ru.otus.hw06.services.CassetteServiceImpl;
import ru.otus.hw06.services.MoneyStorageServiceImpl;

import java.util.EnumMap;
import java.util.List;

public class Starter {
    public static void main(String[] args) {
        EnumMap<Denomination, CassetteService> cassetteServiceEnumMap = new EnumMap<>(Denomination.class);
        for (var den: Denomination.values()){
            cassetteServiceEnumMap.put(den, new CassetteServiceImpl());
        }
        var atm = new ATMImpl(new MoneyStorageServiceImpl(cassetteServiceEnumMap));

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
        System.out.println(atm.depositMoney(banknoteList));
        System.out.println(atm.depositMoney(List.of(new Banknote(Denomination.TWO_HUNDRED))));
        System.out.println(atm.getAvailableMoneyCount());
        System.out.println(atm.withdrawMoney(1_100));
        System.out.println(atm.getAvailableMoneyCount());
    }
}
