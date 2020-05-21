package ru.otus.hw07.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.domain.Banknote;
import ru.otus.hw07.domain.Denomination;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CassetteServiceImplTest {
    private CassetteService cassetteService;
    private final int initialBanknoteCount = 5;

    @BeforeEach
    void setUp() {
        cassetteService = new CassetteServiceImpl();
        List<Banknote> banknotes = Stream.generate(() -> new Banknote(Denomination.ONE_HUNDRED))
                .limit(initialBanknoteCount).collect(Collectors.toList());
        cassetteService.storeBanknotes(banknotes);
    }

    @Test
    void shouldReturnRequestedBanknotes() {
        int banknotesCountToReturn = 3;
        assertEquals(banknotesCountToReturn, cassetteService.retrieveBanknotes(banknotesCountToReturn).size());
        assertEquals(initialBanknoteCount - banknotesCountToReturn, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldNotReturnBanknotesIfUnableTo() {
        int banknotesCountToReturn = initialBanknoteCount + 1;
        assertEquals(0, cassetteService.retrieveBanknotes(banknotesCountToReturn).size());
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldNotReturnBanknotesIfInvalidCount() {
        int banknotesCountToReturn = -1;
        assertEquals(0, cassetteService.retrieveBanknotes(banknotesCountToReturn).size());
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldObtainBanknotes() {
        int banknotesCountToStore = 4;
        List<Banknote> newBanknotes = Stream.generate(() -> new Banknote(Denomination.ONE_HUNDRED))
                .limit(banknotesCountToStore).collect(Collectors.toList());
        cassetteService.storeBanknotes(newBanknotes);
        assertEquals(initialBanknoteCount + banknotesCountToStore, cassetteService.getBanknotesCount());
    }

    @Test
    void getReturnBanknotesCount() {
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }
}