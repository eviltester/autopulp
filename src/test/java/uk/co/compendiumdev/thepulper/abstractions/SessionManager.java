package uk.co.compendiumdev.thepulper.abstractions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/*
    I called this SessionManager, rather than DriverManager
    because I imagine that I might want to re-use the same
    session most of the time and share a session cookie,
    or browser instance.

    Sharing a session cookie would allow the tests to manage
    the driver e.g. close them and avoid having a central
    shutdown hook to close any managed drivers.

    Initially I hard code the browser, but this will quickly
    become configurable. To run tests on different browsers
    using CI.
 */
public class SessionManager {
    public static WebDriver getDriver() {
        return new ChromeDriver();
    }
}
