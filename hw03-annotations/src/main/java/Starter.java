import example.ClassAfterFail;
import example.ClassBeforeFail;
import example.OtherTestClass;
import example.TestClass;
import runner.TestRunner;

public class Starter {
    public static void main(String[] args) {
        new TestRunner<>(TestClass.class).runTests();
        new TestRunner<>(OtherTestClass.class).runTests();
        new TestRunner<>(ClassBeforeFail.class).runTests();
        new TestRunner<>(ClassAfterFail.class).runTests();
    }
}
