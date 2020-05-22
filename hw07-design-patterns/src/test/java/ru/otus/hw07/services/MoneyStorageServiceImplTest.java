package ru.otus.hw07.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.banknotestrategy.StrategyEnum;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;
import ru.otus.hw07.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw07.domain.NotSufficientFundsException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест сервиса хранения денег ")
class MoneyStorageServiceImplTest {
    private MoneyStorageService moneyStorageService;

    @BeforeEach
    public void setUpMoneyStorage() {
        Map<Denomination, CassetteService> cassetteMap = new TreeMap<>();
        for (var den : Denomination.values()) {
            cassetteMap.put(den, new CassetteServiceImpl(0));
        }
        moneyStorageService = new MoneyStorageServiceImpl(cassetteMap);
    }

    @Test
    @DisplayName(" должен пополнить хранилище денег")
    void shouldDeposit() {
        assertEquals(0, moneyStorageService.getAvailableMoneyCount());
        var moneyBundle = List.of(
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
        long expectedSum = moneyBundle.stream().map(banknote -> banknote.getDenomination().getDenominationValue()).reduce(0L, Long::sum);
        assertEquals(expectedSum, moneyStorageService.storeMoney(moneyBundle));
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount());

        expectedSum += 500;
        assertEquals(500, moneyStorageService.storeMoney(List.of(new Banknote(Denomination.FIVE_HUNDRED))));
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount());
    }

    @Test
    void shouldThrowNotSufficientFundsException() {
        long amountThatUnableToGive = Long.MAX_VALUE;
        assertThrows(NotSufficientFundsException.class,
                () -> moneyStorageService.retrieveMoney(StrategyEnum.REGULAR, amountThatUnableToGive));
    }

    @Test
    void shouldThrowNoSuitableBanknotesAvailableException() {
        long amountThatNoBanknotesCanSatisfy = 50;
        moneyStorageService.storeMoney(List.of(new Banknote(Denomination.ONE_HUNDRED)));
        assertThrows(NoSuitableBanknotesAvailableException.class,
                () -> moneyStorageService.retrieveMoney(StrategyEnum.REGULAR, amountThatNoBanknotesCanSatisfy));
    }

    @Test
    @DisplayName("Greedy strategy")
    void shouldThrowNoSuitableBanknotesAvailableExceptionEveryTime() {
        long validAmount = Denomination.ONE_HUNDRED.getDenominationValue();
        moneyStorageService.storeMoney(List.of(new Banknote(Denomination.ONE_HUNDRED)));
        assertThrows(NoSuitableBanknotesAvailableException.class,
                () -> moneyStorageService.retrieveMoney(StrategyEnum.GREEDY, validAmount));
        assertThrows(NoSuitableBanknotesAvailableException.class,
                () -> moneyStorageService.retrieveMoney(StrategyEnum.GREEDY, 0));
    }

    @Test
    @DisplayName(" должен выдать требуемую сумму денег")
    void shouldWithdraw() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        for (var denomination : Denomination.values()) {
            Banknote tmp = new Banknote(denomination);
            moneyStorageService.storeMoney(List.of(tmp));
        }

        assertThat(moneyStorageService.retrieveMoney(StrategyEnum.REGULAR, 100)).hasSize(1)
                .allMatch(x -> x.getDenomination().equals(Denomination.ONE_HUNDRED));

        assertThat(moneyStorageService.retrieveMoney(StrategyEnum.REGULAR, 7_000)).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsAll(List.of(new Banknote(Denomination.FIVE_THOUSAND), new Banknote(Denomination.TWO_THOUSAND)));
    }

    @Test
    @DisplayName(" должен правильно вернуть сумму хранящихся денег")
    void shouldReturnAvailableMoneyCount() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        assertEquals(0, moneyStorageService.getAvailableMoneyCount());
        var moneyBundle = List.of(new Banknote(Denomination.FIFTY), new Banknote(Denomination.FIFTY),
                new Banknote(Denomination.FIVE_HUNDRED),
                new Banknote(Denomination.FIVE_THOUSAND));
        moneyStorageService.storeMoney(moneyBundle);
        int expectedSum = 50 + 50 + 500 + 5_000;
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount());

        moneyStorageService.storeMoney(List.of(new Banknote(Denomination.ONE_THOUSAND)));
        expectedSum += 1_000;
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount());

        moneyStorageService.retrieveMoney(StrategyEnum.REGULAR, 5_000);
        expectedSum -= 5_000;
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount());
    }

    @Test
    public void shouldReturnBanknotesCount() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        for (var denomination : Denomination.values()) {
            assertEquals(0, moneyStorageService.getAvailableBanknotesCount(denomination));

            moneyStorageService.storeMoney(List.of(new Banknote(denomination)));
            assertEquals(1, moneyStorageService.getAvailableBanknotesCount(denomination));

            moneyStorageService.storeMoney(Collections.emptyList());
            assertEquals(1, moneyStorageService.getAvailableBanknotesCount(denomination));

            moneyStorageService.retrieveMoney(StrategyEnum.REGULAR, denomination.getDenominationValue());
            assertEquals(0, moneyStorageService.getAvailableBanknotesCount(denomination));
        }
    }
}