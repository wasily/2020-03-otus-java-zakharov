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
    private final Class<T> clazz;
    private final Report report;

    public TestRunner(Class<T> clazz) {
        this.clazz = clazz;
        this.report = new Report(clazz.getName());
    }

    public void runTests() {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }

        testMethods.stream().parallel().forEach(m -> performTest(m, beforeMethods, afterMethods));

        System.out.println(report.getReportMessage());
        System.out.println(report.getStatistics());
    }

    private void performTest(Method testMethod, List<Method> beforeMethods, List<Method> afterMethods) {
        T testClassInstance = null;
        try {
            testClassInstance = (T) clazz.getDeclaredConstructor().newInstance();
            invokeBeforeMethods(testClassInstance, beforeMethods, testMethod.getName());
            invokeTestMethod(testClassInstance, testMethod);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        } finally {
            if (testClassInstance != null) {
                invokeAfterMethods(testClassInstance, afterMethods, testMethod.getName());
            }
        }
    }


    private void invokeTestMethod(T testClassInstance, Method testMethod) throws InvocationTargetException, IllegalAccessException {
        try {
            testMethod.invoke(testClassInstance);
            reportStatus(testMethod.getName(), testMethod.getName(), ReportEntry.MethodType.TEST, ReportEntry.TestStatus.SUCCESS);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            reportStatus(testMethod.getName(), testMethod.getName(), ReportEntry.MethodType.TEST, ReportEntry.TestStatus.FAILED, ex.getCause().toString());
            throw ex;
        }
    }

    private void invokeBeforeMethods(T testClassInstance, List<Method> methods, String testMethodName) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            try {
                method.invoke(testClassInstance);
                reportStatus(testMethodName, method.getName(), ReportEntry.MethodType.AUX, ReportEntry.TestStatus.SUCCESS);
            } catch (IllegalAccessException | InvocationTargetException e) {
                reportStatus(testMethodName, method.getName(), ReportEntry.MethodType.AUX, ReportEntry.TestStatus.FAILED, e.getCause().toString());
                throw e;
            }
        }
    }

    private void invokeAfterMethods(T testClassInstance, List<Method> methods, String testMethodName) {
        for (Method method : methods) {
            try {
                method.invoke(testClassInstance);
                reportStatus(testMethodName, method.getName(), ReportEntry.MethodType.AUX, ReportEntry.TestStatus.SUCCESS);
            } catch (IllegalAccessException | InvocationTargetException e) {
                reportStatus(testMethodName, method.getName(), ReportEntry.MethodType.AUX, ReportEntry.TestStatus.FAILED, e.getCause().toString());
            }
        }
    }

    private void reportStatus(String key, String methodName, ReportEntry.MethodType methodType, ReportEntry.TestStatus testStatus, String... description) {
        report.addReportEntry(key, new ReportEntry(methodName, methodType, testStatus, String.join(" ", description)));
    }

}
