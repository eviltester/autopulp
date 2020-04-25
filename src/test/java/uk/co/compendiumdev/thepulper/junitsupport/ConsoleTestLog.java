package uk.co.compendiumdev.thepulper.junitsupport;

import org.junit.jupiter.api.TestInfo;

public class ConsoleTestLog {
    private final TestInfo testinfo;
    static String theClassName="";

    public ConsoleTestLog(final TestInfo testinfo) {
        this.testinfo = testinfo;
    }

    public void start() {
        String currentTestClassName = testinfo.getTestClass().get().getName();
        String displayClassName = "";
        if(theClassName.equals(currentTestClassName)){
            displayClassName = "";
        }else{
            displayClassName = "in " + currentTestClassName;
            theClassName = currentTestClassName;
        }
        System.out.println(String.format("Starting Test: %s %n %s - %s - %s", displayClassName, testinfo.getTestClass().get().getSimpleName(), testinfo.getTestMethod().get().getName(), testinfo.getDisplayName()));
    }

    public void stop() {
        // TODO have some timing calculations output
        System.out.println("Ending Test: " + testinfo.getDisplayName());
    }
}
