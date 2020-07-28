package hw15;

import java.util.ArrayList;
import java.util.List;

public class Demonstration {
    private final StringBuilder sb = new StringBuilder();
    private final Generator generator = new Generator(10);
    private String string;
    private static final int THREAD_COUNT = 2;
    private static final int ITERATION_COUNT = 42;

    private String getString() {
        sb.append(generator.getNext()).append(" ");
        return sb.toString();
    }

    public void start() {
        string = getString();
        for (int i = 0; i < ITERATION_COUNT; i++) {
            System.err.println("iteration #" + i);
            List<Thread> threads = new ArrayList<>(THREAD_COUNT);
            for (int j = 0; j < THREAD_COUNT; j++) {
                Thread th = new Thread(() -> System.err.println(Thread.currentThread().getName() + " " + string));
                th.setName("thread" + j);
                threads.add(th);
            }
            threads.forEach(Thread::start);
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            string = getString();
        }
    }
}
