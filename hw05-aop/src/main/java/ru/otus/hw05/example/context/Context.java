package ru.otus.hw05.example.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Context {
    private Context() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getExampleService(T serviceImpl) {
        InvocationHandler handler = new LogInvocationHandler<>(serviceImpl);
        return (T) Proxy.newProxyInstance(Context.class.getClassLoader(), serviceImpl.getClass().getInterfaces(), handler);
    }
}
