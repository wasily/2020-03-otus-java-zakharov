package ru.otus.hw05.example;

import ru.otus.hw05.annotation.Log;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class Context {
    public Context() {
    }

    public static ExampleService getExampleService() {
        InvocationHandler handler = new DemoInvocationHandler(new ExampleServiceImpl());
        return (ExampleService) Proxy.newProxyInstance(Context.class.getClassLoader(), new Class<?>[]{ExampleService.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final ExampleService myClass;
        private List<String> annotatedMethodsSignatures;

        DemoInvocationHandler(ExampleService myClass) {
            this.myClass = myClass;
            this.annotatedMethodsSignatures = new ArrayList<>();
            for (Method m : myClass.getClass().getMethods()) {
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
            return method.invoke(myClass, args);
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
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
