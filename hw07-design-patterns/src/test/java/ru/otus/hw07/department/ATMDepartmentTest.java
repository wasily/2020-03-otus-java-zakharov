package ru.otus.hw07.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.services.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMDepartmentTest {
    private ATMDepartment atmDepartment;
    private ATM atm1;
    private ATM atm2;
    private ATM atm3;
    private final int initialATMCount = 3;

    @BeforeEach
    void setUp() {
        atm1 = mock(ATM.class);
        atm2 = mock(ATM.class);
        atm3 = mock(ATM.class);
        Set<ATM> atmSet = new HashSet<>();
        atmSet.add(atm1);
        atmSet.add(atm2);
        atmSet.add(atm3);
        atmDepartment = new ATMDepartment(atmSet);
    }

    @Test
    void shouldThrowUnsupportedOperationExceptionWhenDepositMoney() {
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.depositMoney(List.of(new Banknote(Denomination.FIVE_THOUSAND))));
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.depositMoney(Collections.emptyList()));
    }

    @Test
    void shouldThrowUnsupportedOperationExceptionWhenWithdrawMoney() {
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.withdrawMoney(StrategyEnum.REGULAR, 0));
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.withdrawMoney(StrategyEnum.REGULAR, 100));
    }

    @Test
    void shouldReturnAvailableMoneyCount() {
        long atm1Balance = 1_000;
        long atm2Balance = 11_000;
        long atm3Balance = 150_000;
        when(atm1.getAvailableMoneyCount()).thenReturn(atm1Balance);
        when(atm2.getAvailableMoneyCount()).thenReturn(atm2Balance);
        when(atm3.getAvailableMoneyCount()).thenReturn(atm3Balance);
        assertEquals(atm1Balance + atm2Balance + atm3Balance, atmDepartment.getAvailableMoneyCount());
    }

    @Test
    void shouldAddATMToDepartment() {
        ATM newATM = mock(ATM.class);
        int ATMCount = initialATMCount;
        atmDepartment.addATM(newATM);
        assertEquals(++ATMCount, atmDepartment.getATMCount());

        atmDepartment.addATM(newATM);
        assertEquals(ATMCount, atmDepartment.getATMCount());
    }

    @Test
    void shouldRemoveATMFromDepartment() {
        int ATMCount = initialATMCount;
        ATM atmToRemove = atm1;
        assertEquals(ATMCount, atmDepartment.getATMCount());

        atmDepartment.removeATM(atmToRemove);
        assertEquals(--ATMCount, atmDepartment.getATMCount());

        atmDepartment.removeATM(atmToRemove);
        assertEquals(ATMCount, atmDepartment.getATMCount());
    }

    @Test
    void shouldSaveAndRestoreATMs() {
        int initialBanknotesCount1 = 5;
        int initialBanknotesCount2 = 6;
        int initialBanknotesCount3 = 7;
        Map<Denomination, CassetteService> cassetteMap1 = new TreeMap<>();
        Map<Denomination, CassetteService> cassetteMap2 = new TreeMap<>();
        Map<Denomination, CassetteService> cassetteMap3 = new TreeMap<>();
        for (var den : Denomination.values()) {
            cassetteMap1.put(den, new CassetteServiceImpl(initialBanknotesCount1));
            cassetteMap2.put(den, new CassetteServiceImpl(initialBanknotesCount2));
            cassetteMap3.put(den, new CassetteServiceImpl(initialBanknotesCount3));
        }
        ATM atm1 = new ATMImpl(new MoneyStorageServiceImpl(cassetteMap1));
        ATM atm2 = new ATMImpl(new MoneyStorageServiceImpl(cassetteMap2));
        ATM atm3 = new ATMImpl(new MoneyStorageServiceImpl(cassetteMap3));
        ATMDepartment atmDep = new ATMDepartment(Set.of(atm1, atm2, atm3));
        atmDep.saveATMs();
        long beforeBalance = atmDep.getAvailableMoneyCount();
        List<Banknote> list1 = List.of(new Banknote(Denomination.TWO_THOUSAND), new Banknote(Denomination.FIVE_HUNDRED));
        List<Banknote> list2 = List.of(new Banknote(Denomination.FIVE_THOUSAND), new Banknote(Denomination.ONE_HUNDRED));
        List<Banknote> list3 = List.of(new Banknote(Denomination.TWO_HUNDRED), new Banknote(Denomination.TWO_THOUSAND));
        atm1.depositMoney(list1);
        atm2.depositMoney(list2);
        atm3.depositMoney(list3);
        long sum = list1.stream().mapToLong(x -> x.getDenomination().getDenominationValue()).sum() +
                list2.stream().mapToLong(x -> x.getDenomination().getDenominationValue()).sum() +
                list3.stream().mapToLong(x -> x.getDenomination().getDenominationValue()).sum();
        assertEquals(beforeBalance + sum, atmDep.getAvailableMoneyCount());
        atmDep.resetATMs();
        long afterBalance = atmDep.getAvailableMoneyCount();
        assertEquals(beforeBalance, afterBalance);
    }

    @Test
    void shouldReturnATMCount() {
        assertEquals(initialATMCount, atmDepartment.getATMCount());
    }

    @Test
    void shouldThrowUnsupportedOperationExceptionWhenCopy() {
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.copy());
    }
}