package ru.otus.hw06.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.domain.Banknote;
import ru.otus.hw06.domain.Denomination;

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
        int countToTake = 3;
        assertEquals(countToTake, cassetteService.retrieveBanknotes(countToTake).size());
        assertEquals(initialBanknoteCount - countToTake, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldNotReturnBanknotesIfUnableTo() {
        int countToReturn = initialBanknoteCount + 1;
        assertEquals(0, cassetteService.retrieveBanknotes(countToReturn).size());
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldNotReturnBanknotesIfInvalidCount() {
        int countToReturn = -1;
        assertEquals(0, cassetteService.retrieveBanknotes(countToReturn).size());
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldObtainBanknotes() {
        int countToObtain = 4;
        List<Banknote> newBanknotes = Stream.generate(() -> new Banknote(Denomination.ONE_HUNDRED))
                .limit(countToObtain).collect(Collectors.toList());
        cassetteService.storeBanknotes(newBanknotes);
        assertEquals(initialBanknoteCount + countToObtain, cassetteService.getBanknotesCount());
    }

    @Test
    void getReturnBanknotesCount() {
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }
}