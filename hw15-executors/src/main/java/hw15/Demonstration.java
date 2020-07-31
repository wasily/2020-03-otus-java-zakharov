package hw15;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Demonstration {
    private final StringBuilder sb = new StringBuilder();
    private final Generator generator = new Generator(10);
    private static final int THREAD_COUNT = 2;
    String message = getString();

    private String getString() {
        sb.append(generator.getNext()).append(" ");
        return sb.toString();
    }

    public void start() {
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () -> message = getString());
        Thread th1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " " + message);
                    Thread.sleep(500);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        th1.setName("thread#1");
        Thread th2 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " " + message);
                    Thread.sleep(500);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        th2.setName("thread#2");
        th1.start();
        th2.start();
        while (true) {
            if (barrier.isBroken()) {
                barrier.reset();
            }
        }
    }
}
