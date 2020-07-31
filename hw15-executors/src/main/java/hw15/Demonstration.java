package hw15;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demonstration {
    private final StringBuilder sb = new StringBuilder();
    private final Generator generator = new Generator(10);
    String message = getString();
    private boolean isTh1Turn = true;

    private String getString() {
        sb.append(generator.getNext()).append(" ");
        return sb.toString();
    }

    private void changeTurn() {
        if (!isTh1Turn) {
            message = getString();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isTh1Turn = !isTh1Turn;
    }

    public void start() {
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        Thread th1 = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    while (!isTh1Turn) {
                        try {
                            condition1.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " " + message);
                    condition2.signalAll();
                } finally {
                    changeTurn();
                    lock.unlock();
                }
            }
        });
        th1.setName("thread#1");
        Thread th2 = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    while (isTh1Turn) {
                        try {
                            condition2.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " " + message);
                    condition1.signalAll();
                } finally {
                    changeTurn();
                    lock.unlock();
                }
            }
        });
        th2.setName("thread#2");
        th1.start();
        th2.start();
    }
}
