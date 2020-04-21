package ru.otus.hw03.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Report {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final String testClassName;
    private final ConcurrentMap<String, List<ReportEntry>> entries = new ConcurrentHashMap<>();

    public Report(String testClassName) {
        this.testClassName = testClassName;
    }

    public void addReportEntry(String key, ReportEntry reportEntry) {
        if (entries.containsKey(key)) {
            entries.get(key).add(reportEntry);
        } else {
            List<ReportEntry> tmp = new ArrayList<>();
            tmp.add(reportEntry);
            entries.put(key, tmp);
        }
    }

    public String getReportMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Class ").append(testClassName).append(" test report:").append(LINE_SEPARATOR);
        entries.values().stream()
                .flatMap(Collection::stream).
                forEach((e) -> stringBuilder
                        .append("method ")
                        .append(e.getTestMethodName()).append(" ")
                        .append(e.getTestStatus()).append(" ")
                        .append(e.getDescription() != null ? e.getDescription() : "")
                        .append(LINE_SEPARATOR));
        stringBuilder.append("------------------------");
        return stringBuilder.toString();
    }

    public String getStatistics() {
        long successCnt = entries.values().stream()
                .flatMap(Collection::stream)
                .filter(e -> e.getMethodType().equals(ReportEntry.MethodType.TEST))
                .filter(e -> e.getTestStatus().equals(ReportEntry.TestStatus.SUCCESS))
                .count();
        long failedCnt = entries.values().stream()
                .flatMap(Collection::stream)
                .filter(e -> e.getMethodType().equals(ReportEntry.MethodType.TEST))
                .filter(e -> e.getTestStatus().equals(ReportEntry.TestStatus.FAILED))
                .count();
        long cnt = entries.size();
        return String.format("Completed %d tests of %s, successfully completed - %d, failed - %d" + LINE_SEPARATOR, cnt, testClassName, successCnt, failedCnt);
    }
}
