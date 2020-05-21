package ru.otus.hw07.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CassetteServiceImplTest {
    private CassetteService cassetteService;
    private final int initialBanknoteCount = 5;

    @BeforeEach
    void setUp() {
        cassetteService = new CassetteServiceImpl(initialBanknoteCount);
    }

    @Test
    void shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CassetteServiceImpl(-1));
    }

    @Test
    void shouldReturnRequestedBanknotes() {
        int banknotesCountToReturn = 3;
        cassetteService.retrieveBanknotes(banknotesCountToReturn);
        assertEquals(initialBanknoteCount - banknotesCountToReturn, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldNotReturnBanknotesIfUnableTo() {
        int banknotesCountToReturn = initialBanknoteCount + 1;
        cassetteService.retrieveBanknotes(banknotesCountToReturn);
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldNotReturnBanknotesIfInvalidCount() {
        int banknotesCountToReturn = -1;
        cassetteService.retrieveBanknotes(banknotesCountToReturn);
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }

    @Test
    void shouldObtainBanknotes() {
        int banknotesCountToStore = 4;
        cassetteService.storeBanknotes(banknotesCountToStore);
        assertEquals(initialBanknoteCount + banknotesCountToStore, cassetteService.getBanknotesCount());
    }

    @Test
    void getReturnBanknotesCount() {
        assertEquals(initialBanknoteCount, cassetteService.getBanknotesCount());
    }
}