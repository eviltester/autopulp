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

    This class is very configurable - which also makes it risky,
    after all - have I tested this enough? Answer: NO... I have
    relied on usage for testing it.

    I can configure it via system properties, which is usually good
    enough for build configuration but can be a bit of a pain for
    adhoc configuration during testing itself. I have tried to
    minimise this by checking the System Property configuration
    when it is used, rather than statically when the class is accessed.
    This allows me to set the system properties from code if need be...
    as I do in the CanReuseSessions test. for testing that the SessionManager
    can reuse sessions on different clean browsers.

    The configuration allows choosing the browser type, sharing browsers, sharing sessions across multiple
    browsers.

    The methods allow getting a default browser managed by session manager.

    Getting an unmanaged browser which has a shared session
    i.e. a the same user opening multiple browsers

    Getting an completely clean unmanaged browser, of the type defined by the
    property.

    I could add more to get a specific type of browser e.g. "chrome" etc. but
    I will add that when I need it.

    Note, that because I am sharing the browsers, I can't really let test code
    driver.quit() so I have to have a SessionManager.quit(driver) method which
    only quits it if it is not the shared driver.

    And... because no test can quit the browsers, I have to have a shutdown hook
    that will close the driver when all my tests are finished and my SessionManager
    is garbage collected in clean up, otherwise I would have a rogue browser on screen.

    TODO: I imagine I'm going to need a cleanStart or 'refresh' method so that we still use
    a shared browser, but it is clean.

    TODO: this class will work well for single threaded test execution. As soon as I have
    parallel execution, this approach does not work well unless I take account of the
    current thread that called the SessionManager and track shared browsers across threads
    this is possible, but I haven't needed to do that yet.

    Under those circumstances I would probably not share the driver and set the system
    property as required.

    You can imagine that this class eventually becomes quite complicated based on the needs
    of the project. Which is why it is best not to make this too generic, and instead to
    build it as required, and try to keep your requirements simple.
 */
public class SessionManager {

    // this was how I checked that the property was working
    // now I can set the property externally and run the tests
    // or manually use this from within code and then I don't have
    // to keep changing the default
    // static String setBrowserTo = System.setProperty("autopulp.browser", "firefox");
    //static String setReuseSessionTo = System.setProperty("autopulp.reuseSession", "false");
    //static String setShareDriverTo = System.setProperty("autopulp.shareDriver", "false");

    static Cookie sessionCookie;
    static Cookie apiCookie;
    static Boolean reuseSession;

    // if we share a driver then we have to use SessionManager.quitDriver to close that driver
    // using driver.quit will 'break' this
    static Boolean shareDriver=false;
    static WebDriver sharedDriver;

    public static WebDriver getDriver() {

        // if we are sharing a driver and have initiated it
        // then return it
        // TODO: we should really check if driver is working
        // and if not, create a new one
        shareDriver = Boolean.parseBoolean(System.getProperty("autopulp.shareDriver", "true"));

        if(shareDriver){
            if(sharedDriver!=null){
                // just because we are using the same driver
                // does not mean we need the same session
                // TODO: should delete any local storage as well
                if(!reuseSession){
                    sharedDriver.manage().deleteCookieNamed("JSESSIONID");
                    sharedDriver.manage().deleteCookieNamed("X-API-AUTH");
                }
                return sharedDriver;
            }
        }

        WebDriver driver = getUnmanagedDriver();


        if(shareDriver){
            if(sharedDriver==null){
                // now that we are sharing the driver, we need to close it
                // when we finish all the tests
                Runtime.getRuntime().addShutdownHook(new Thread()
                {
                    public void run()
                    {
                        try {
                            System.out.println("Closing Shared Driver");
                            sharedDriver.quit();
                            sharedDriver=null;
                        }catch (Exception e){

                        }
                    }
                });


                sharedDriver=driver;
            }
        }

        return driver;
    }

    private static void useSharedSession(final WebDriver driver) {

        reuseSession = Boolean.parseBoolean(System.getProperty("autopulp.reuseSession", "true"));

        // cookie sharing to support session sharing
        if(reuseSession) {
            driver.get(AppEnvironment.baseUrl());
            if (sessionCookie == null) {
                sessionCookie = driver.manage().getCookieNamed("JSESSIONID");
            } else {
                driver.manage().deleteCookieNamed("JSESSIONID");
                driver.manage().addCookie(sessionCookie);
            }
            if (apiCookie == null) {
                apiCookie = driver.manage().getCookieNamed("X-API-AUTH");
            } else {
                driver.manage().deleteCookieNamed("X-API-AUTH");
                driver.manage().addCookie(apiCookie);
            }
        }
    }

    public static void quit(WebDriver driver){
        // if we are sharing the driver and this
        // is the driver we are sharing then do not
        // close it
        if(shareDriver && driver==sharedDriver){
            return;
        }

        // I am not managing this driver, just quit
        driver.quit();
    }

    public static WebDriver getCleanUnmanagedDriver() {
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

        return driver;
    }

    public static WebDriver getUnmanagedDriver() {
        WebDriver driver = getCleanUnmanagedDriver();
        useSharedSession(driver);
        return driver;
    }
}
