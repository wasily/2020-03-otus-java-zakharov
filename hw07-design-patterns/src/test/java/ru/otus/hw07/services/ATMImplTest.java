package ru.otus.hw07.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMImplTest {
    private MoneyStorageService moneyStorageService;
    private ATM atm;

    @BeforeEach
    void setup() {
        moneyStorageService = mock(MoneyStorageService.class);
        atm = new ATMImpl(moneyStorageService);
    }

    @Test
    void shouldDepositMoney() {
        var banknotes = List.of(new Banknote(Denomination.TEN),
                new Banknote(Denomination.FIVE_HUNDRED),
                new Banknote(Denomination.FIVE_HUNDRED));
        long expectedSum = banknotes.stream().map(Banknote::getValue).reduce(0L, Long::sum);
        when(moneyStorageService.storeMoney(banknotes)).thenReturn(expectedSum);
        assertEquals(expectedSum, atm.depositMoney(banknotes));
    }

    @Test
    void shouldWithdrawMoney() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        var expectedMoney = List.of(new Banknote(Denomination.TWO_THOUSAND),
                new Banknote(Denomination.TWO_HUNDRED),
                new Banknote(Denomination.TEN));
        long sum = expectedMoney.stream().map(Banknote::getValue).reduce(0L, Long::sum);
        when(moneyStorageService.retrieveMoney(sum)).thenReturn(expectedMoney);
        Assertions.assertThat(atm.withdrawMoney(sum)).hasSize(3).containsAll(expectedMoney);
    }

    @Test
    void shouldReturnAvailableMoneyCount() {
        long expectedBalance = 1984;
        when(moneyStorageService.getAvailableMoneyCount()).thenReturn(BigInteger.valueOf(expectedBalance));
        assertEquals(BigInteger.valueOf(expectedBalance), atm.getAvailableMoneyCount());
    }
}