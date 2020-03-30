package ru.otus;

import com.google.common.base.MoreObjects;

public class HelloOtus {
    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        System.out.println(MoreObjects.toStringHelper(t)
                .add("name", t.getName())
                .add("priority", t.getPriority())
                .toString());
    }
}
