package ru.otus.hw06.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.domain.Banknote;
import ru.otus.hw06.domain.Denomination;
import ru.otus.hw06.domain.NoSuitableBanknotesAvailableException;
import ru.otus.hw06.domain.NotSufficientFundsException;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест сервиса хранения денег ")
class MoneyStorageServiceImplTest {
    private MoneyStorageService moneyStorageService;
    private final EnumMap<Denomination, CassetteService> cassetteServiceEnumMap = new EnumMap<>(Denomination.class);

    @BeforeEach
    public void setUpMoneyStorage() {
        moneyStorageService = new MoneyStorageServiceImpl(cassetteServiceEnumMap);
        for (var den : Denomination.values()) {
            cassetteServiceEnumMap.put(den, new CassetteServiceImpl());
        }
    }

    @Test
    @DisplayName(" должен пополнить хранилище денег")
    void shouldDeposit() {
        assertEquals(0, moneyStorageService.getAvailableMoneyCount().longValue());
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
        long expectedSum = moneyBundle.stream().map(Banknote::getValue).reduce(0L, Long::sum);
        assertEquals(expectedSum, moneyStorageService.storeMoney(moneyBundle));
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount().longValue());

        expectedSum += 500;
        assertEquals(500, moneyStorageService.storeMoney(List.of(new Banknote(Denomination.FIVE_HUNDRED))));
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount().longValue());
    }

    @Test
    void shouldThrowNotSufficientFundsException() {
        long amountThatUnableToGive = Long.MAX_VALUE;
        assertThrows(NotSufficientFundsException.class,
                () -> moneyStorageService.retrieveMoney(amountThatUnableToGive));
    }

    @Test
    void shouldThrowNoSuitableBanknotesAvailableException() {
        long amountThatNoBanknotesCanSatisfy = 50;
        moneyStorageService.storeMoney(List.of(new Banknote(Denomination.ONE_HUNDRED)));
        assertThrows(NoSuitableBanknotesAvailableException.class,
                () -> moneyStorageService.retrieveMoney(amountThatNoBanknotesCanSatisfy));
    }

    @Test
    @DisplayName(" должен выдать требуемую сумму денег")
    void shouldWithdraw() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        for (var denomination : Denomination.values()) {
            Banknote tmp = new Banknote(denomination);
            moneyStorageService.storeMoney(List.of(tmp));
        }

        assertThat(moneyStorageService.retrieveMoney(100)).hasSize(1)
                .allMatch(x -> x.getDenomination().equals(Denomination.ONE_HUNDRED));

        assertThat(moneyStorageService.retrieveMoney(7_000)).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsAll(List.of(new Banknote(Denomination.FIVE_THOUSAND), new Banknote(Denomination.TWO_THOUSAND)));
    }

    @Test
    @DisplayName(" должен правильно вернуть сумму хранящихся денег")
    void shouldReturnAvailableMoneyCount() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        assertEquals(0, moneyStorageService.getAvailableMoneyCount().longValue());
        var moneyBundle = List.of(new Banknote(Denomination.FIFTY), new Banknote(Denomination.FIFTY),
                new Banknote(Denomination.FIVE_HUNDRED),
                new Banknote(Denomination.FIVE_THOUSAND));
        moneyStorageService.storeMoney(moneyBundle);
        int expectedSum = 50 + 50 + 500 + 5_000;
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount().longValue());

        moneyStorageService.storeMoney(List.of(new Banknote(Denomination.ONE_THOUSAND)));
        expectedSum += 1_000;
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount().longValue());

        moneyStorageService.retrieveMoney(5_000);
        expectedSum -= 5_000;
        assertEquals(expectedSum, moneyStorageService.getAvailableMoneyCount().longValue());
    }

    @Test
    public void shouldReturnBanknotesCount() throws NotSufficientFundsException, NoSuitableBanknotesAvailableException {
        for (var denomination : Denomination.values()) {
            assertEquals(0, moneyStorageService.getAvailableBanknotesCount(denomination));

            moneyStorageService.storeMoney(List.of(new Banknote(denomination)));
            assertEquals(1, moneyStorageService.getAvailableBanknotesCount(denomination));

            moneyStorageService.storeMoney(Collections.emptyList());
            assertEquals(1, moneyStorageService.getAvailableBanknotesCount(denomination));

            moneyStorageService.retrieveMoney(denomination.getDenominationValue());
            assertEquals(0, moneyStorageService.getAvailableBanknotesCount(denomination));
        }
    }
}