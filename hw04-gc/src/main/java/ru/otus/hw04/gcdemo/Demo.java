package ru.otus.hw04.gcdemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.hw04.service.CacheService;
import ru.otus.hw04.service.CacheServiceImpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MICROS;

public class Demo {
    private int cacheSize = 40_000_000;
    private float cacheUpdateRatio = 0.1f;
    private static final Logger cacheUpdateLogger = LogManager.getLogger("cacheUpdateLogger");
    private static final Logger largeAllocationLogger = LogManager.getLogger("largeAllocationLogger");
    private static final Logger cacheQueryLogger = LogManager.getLogger("cacheQueryLogger");

    public void start() {
        CacheService<Integer, String> cacheService = new CacheServiceImpl<>(cacheSize);
        List<String> blackhole = new ArrayList<>(60_000_000);

        Thread cacheUpdateThread = new Thread(() -> {
            for (int i = 0; i < cacheSize; i++) {
                cacheService.put(i, String.valueOf(Math.random() * i));
            }
            while (true) {
                sleep(10_000);
                LocalTime before = LocalTime.now();
                for (int i = 0; i < cacheSize * cacheUpdateRatio; i++) {
                    cacheService.put(i, String.valueOf(Math.random() * i));
                }
                LocalTime after = LocalTime.now();
                cacheUpdateLogger.info("cache updated (μs): " + MICROS.between(before, after));
            }
        });

        Thread largeAllocationThread = new Thread(() -> {
            for (int i = 0; i < 42; i++) {
                sleep(30_000);
                LocalTime before = LocalTime.now();
                for (int j = 0; j < 10_000_000; j++) {
                    blackhole.add(String.valueOf(LocalDateTime.now()));
                }
                LocalTime after = LocalTime.now();
                largeAllocationLogger.info("largeAllocation completed in (μs): " + MICROS.between(before, after));
            }
        });

        Thread cacheQueryThread = new Thread(() -> {
            int queryCnt = 10_000;
            while (true) {
                sleep(5_000);
                LocalTime before = LocalTime.now();
                for (int i = 0; i < queryCnt; i++) {
                    cacheService.get((int) (Math.random() * cacheSize));
                }
                LocalTime after = LocalTime.now();
                cacheQueryLogger.info(queryCnt + " queries completed in (μs): " + MICROS.between(before, after));
            }
        });

        largeAllocationThread.setName("largeAllocationThread");
        largeAllocationThread.start();
        cacheUpdateThread.setName("cacheUpdateThread");
        cacheUpdateThread.start();
        cacheQueryThread.setName("cacheQueryThread");
        cacheQueryThread.start();
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
