package uk.co.compendiumdev.thepulper.abstractions;

import org.openqa.selenium.Cookie;
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
    //static String setReuseSessionTo = System.setProperty("autopulp.reuseSession", "false");

    static Cookie sessionCookie;
    static Boolean reuseSession = Boolean.parseBoolean(System.getProperty("autopulp.reuseSession", "true"));

    public static WebDriver getDriver() {

        String chosenBrowser = System.getProperty("autopulp.browser", "chrome");
        WebDriver driver;

        switch (chosenBrowser){
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                driver = new ChromeDriver();
        }

        // cookie sharing to support session sharing
        if(reuseSession) {
            driver.get(AppEnvironment.baseUrl());
            if (sessionCookie == null) {
                sessionCookie = driver.manage().getCookieNamed("JSESSIONID");
            } else {
                driver.manage().deleteCookieNamed("JSESSIONID");
                driver.manage().addCookie(sessionCookie);
            }
        }

        return driver;
    }
}
