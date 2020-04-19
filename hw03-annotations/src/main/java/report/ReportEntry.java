package report;

import lombok.Getter;

@Getter
public class ReportEntry {
    private String testMethodName;
    private MethodType methodType;
    private TestStatus testStatus;
    private String description;

    public ReportEntry(String testMethodName, MethodType methodType, TestStatus testStatus) {
        this.testMethodName = testMethodName;
        this.methodType = methodType;
        this.testStatus = testStatus;
    }

    public ReportEntry(String testMethodName, MethodType methodType, TestStatus testStatus, String description) {
        this.testMethodName = testMethodName;
        this.methodType = methodType;
        this.testStatus = testStatus;
        this.description = description;
    }

    public enum MethodType{
        TEST,
        AUX
    }

    public enum TestStatus {
        SUCCESS,
        FAILED
    }
}
