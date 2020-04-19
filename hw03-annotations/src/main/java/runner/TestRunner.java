package runner;

import annotations.After;
import annotations.Before;
import annotations.Test;
import report.Report;
import report.ReportEntry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner<T> {
    private List<Method> beforeMethods = new ArrayList<>();
    private List<Method> testMethods = new ArrayList<>();
    private List<Method> afterMethods = new ArrayList<>();
    private final Class<T> clazz;
    private final Report report;

    public TestRunner(Class<T> clazz) {
        this.clazz = clazz;
        this.report = new Report(clazz.getName());
    }

    public void runTests() {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }

        testMethods.stream().parallel().forEach(this::performTest);

        System.out.println(report.getReportMessage());
        System.out.println(report.getStatistics());
    }

    private void performTest(Method testMethod) {
        try {
            T testClassInstance = (T) clazz.getDeclaredConstructor().newInstance();
            invokeMethods(testClassInstance, beforeMethods, testMethod.getName());
            invokeTestMethod(testClassInstance, testMethod);
            invokeMethods(testClassInstance, afterMethods, testMethod.getName());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        }
    }


    private void invokeTestMethod(T testClassInstance, Method testMethod) throws IllegalAccessException, InvocationTargetException {
        try {
            testMethod.invoke(testClassInstance);
            report.addReportEntry(testMethod.getName(), new ReportEntry(testMethod.getName(), ReportEntry.MethodType.TEST, ReportEntry.TestStatus.SUCCESS));
        } catch (InvocationTargetException ex) {
            report.addReportEntry(testMethod.getName(), new ReportEntry(testMethod.getName(), ReportEntry.MethodType.TEST, ReportEntry.TestStatus.FAILED, ex.getCause().toString()));
            throw ex;
        }
    }

    private void invokeMethods(T testClassInstance, List<Method> methods, String testMethodName) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            try {
                method.invoke(testClassInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                report.addReportEntry(testMethodName, new ReportEntry(method.getName(), ReportEntry.MethodType.AUX, ReportEntry.TestStatus.FAILED, e.getCause().toString()));
                throw e;
            }
        }
    }

}
