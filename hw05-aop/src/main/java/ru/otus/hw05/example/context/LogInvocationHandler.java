package ru.otus.hw05.example.context;

import ru.otus.hw05.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

public class LogInvocationHandler<T> implements InvocationHandler {
    private final T service;
    private final Set<String> annotatedMethodsSignatures;

    LogInvocationHandler(T service) {
        this.service = service;
        this.annotatedMethodsSignatures = new HashSet<>();
        for (Method m : service.getClass().getMethods()) {
            if (m.isAnnotationPresent(Log.class)) {
                annotatedMethodsSignatures.add(getMethodIdentifier(m));
            }
        }
    }

    private String getMethodIdentifier(Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.getName());
        stringBuilder.append("(");
        for (Parameter parameter : method.getParameters()) {
            stringBuilder.append(parameter.getType().getName()).append(",");
        }
        if (stringBuilder.lastIndexOf(",") != -1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (annotatedMethodsSignatures.contains(getMethodIdentifier(method))) {
            logExecutionEvent(method, args);
        }
        return method.invoke(service, args);
    }

    private void logExecutionEvent(Method method, Object[] args) {
        System.out.println("-----------------------");
        System.out.println("executed method:" + method + " params: ");
        for (int i = 0; i < method.getParameterCount(); i++) {
            System.out.printf("%-25s%-10s%-25s%n",
                    method.getParameters()[i].getType().getName(),
                    method.getParameters()[i].getName(),
                    args[i]);
        }
        System.out.println("-----------------------");
    }

    @Override
    public String toString() {
        return "LogInvocationHandler{" +
                "service =" + service +
                '}';
    }
}
