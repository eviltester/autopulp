package uk.co.compendiumdev.thepulper.abstractions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

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

    // this was how I checked that the property was working
    // now I can set the property externally and run the tests
    // or manually use this from within code and then I don't have
    // to keep changing the default
    // static String setBrowserTo = System.setProperty("autopulp.browser", "firefox");

    public static WebDriver getDriver() {

        String chosenBrowser = System.getProperty("autopulp.browser", "chrome");

        switch (chosenBrowser){
            case "chrome":
                return new ChromeDriver();
            case "firefox":
                return new FirefoxDriver();
            default:
                return new ChromeDriver();
        }
    }
}
