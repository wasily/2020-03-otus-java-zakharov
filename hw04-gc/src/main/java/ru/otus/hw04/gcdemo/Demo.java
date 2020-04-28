package ru.otus.hw04.gcdemo;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.hw04.service.CacheService;
import ru.otus.hw04.service.CacheServiceImpl;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MICROS;

public class Demo {
    private final int cacheSize;
    private final int blackholeInitSize;
    private float cacheUpdateRatio = 0.1f;
    private final Logger cacheUpdateLogger = LogManager.getLogger("cacheUpdateLogger");
    private final Logger largeAllocationLogger = LogManager.getLogger("largeAllocationLogger");
    private final Logger cacheQueryLogger = LogManager.getLogger("cacheQueryLogger");
    private final static Logger JMXLoggerLogger = LogManager.getLogger("JMXLogger");

    public Demo(int cacheSize, int blackholeInitSize) {
        this.cacheSize = cacheSize;
        this.blackholeInitSize = blackholeInitSize;
    }

    public void start() {
        CacheService<Integer, String> cacheService = new CacheServiceImpl<>(cacheSize);
        List<String> blackhole = new ArrayList<>(blackholeInitSize);

        switchOnJMXMonitoring();

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
                for (int j = 0; j < blackholeInitSize / 6; j++) {
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

    private static void switchOnJMXMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            JMXLoggerLogger.info("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    JMXLoggerLogger.info("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
