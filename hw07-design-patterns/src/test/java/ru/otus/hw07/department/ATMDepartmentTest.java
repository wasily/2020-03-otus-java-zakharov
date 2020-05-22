package ru.otus.hw07.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.services.ATM;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMDepartmentTest {
    private ATMDepartment atmDepartment;
    private ATM atm1;
    private ATM atm2;
    private ATM atm3;

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
    void depositMoney() {
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.depositMoney(List.of(new Banknote(Denomination.FIVE_THOUSAND))));
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.depositMoney(Collections.emptyList()));
    }

    @Test
    void withdrawMoney() {
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.withdrawMoney(StrategyEnum.REGULAR, 0));
        assertThrows(UnsupportedOperationException.class,
                () -> atmDepartment.withdrawMoney(StrategyEnum.REGULAR, 100));
    }

    @Test
    void getAvailableMoneyCount() {
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
        int ATMCount = 3;
        atmDepartment.addATM(newATM);
        assertEquals(++ATMCount, atmDepartment.getATMCount());

        atmDepartment.addATM(newATM);
        assertEquals(ATMCount, atmDepartment.getATMCount());
    }

    @Test
    void shouldRemoveATMFromDepartment() {
        int ATMCount = 3;
        ATM atmToRemove = atm1;
        assertEquals(ATMCount, atmDepartment.getATMCount());

        atmDepartment.removeATM(atmToRemove);
        assertEquals(--ATMCount, atmDepartment.getATMCount());

        atmDepartment.removeATM(atmToRemove);
        assertEquals(ATMCount, atmDepartment.getATMCount());
    }

    @Test
    void resetATMs() {
    }
}