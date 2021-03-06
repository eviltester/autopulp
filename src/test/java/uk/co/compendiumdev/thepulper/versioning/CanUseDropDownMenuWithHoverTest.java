package uk.co.compendiumdev.thepulper.versioning;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;
import uk.co.compendiumdev.thepulper.junitsupport.ConsoleTestLog;

import java.util.stream.IntStream;

/*
    Because my nav tests are using a 'fake hover' I wanted a test that actually
    does hover, and uses Action to do it, but this means I can't use the mouse when
    this is running.
 */
@Disabled("travis hates this test - I guess the lesson is, avoid actions on travis")
public class CanUseDropDownMenuWithHoverTest {

    private String url;
    private WebDriver driver;
    ConsoleTestLog testLog;

    @BeforeEach
    public void setupBrowser(TestInfo testinfo){
        url = AppEnvironment.baseUrl();
        driver = SessionManager.getUnmanagedDriver();
        testLog = new ConsoleTestLog(testinfo);
        testLog.start();
    }

    static IntStream allPulperVersions() {
        // TODO: when working on travis ci,
        // this does not work for all versions and starts saying it can't find elements
        // I think this is related to travis since it works locally
        // so I have amended this test to just use 1 version
        // I need to look into how to make WebDriver more reliable
        // on Travis or find a different cloud execution system
        //return IntStream.rangeClosed(1, 1);
        return IntStream.rangeClosed(1, ThePulperApp.MAXVERSION);
    }


    @DisplayName("Use an action Hover to check can use menu")
    @ParameterizedTest(name = "using version {0}")
    @MethodSource("allPulperVersions")
    public void clickHelpOnAllVersionsAfterHover(int version){

        driver.get(url + "?v=" + version);

        // get this pointer away from nav - needed to get this working
        // reliably on Firefox
        // hover on admin to drop menu down
        // instead of moving then open new window
//        WebElement admin = new WebDriverWait(driver, 10).
//                until(ExpectedConditions.elementToBeClickable(By.linkText("Admin")));
//        new Actions(driver).moveToElement(admin).perform();
//        admin = admin.findElement(By.xpath(".."));
//
//        // find v001
//        final WebElement v1 = new WebDriverWait(driver, 10).until(
//                ExpectedConditions.elementToBeClickable(
//                        admin.findElement(By.linkText("v001"))
//                )
//        );

        WebElement home = new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(By.linkText("Home")));

        // hover on Home to drop menu down
        new Actions(driver).moveToElement(home).perform();
        home = home.findElement(By.xpath("..")); // get parent li

        // find Help
        final WebElement help = new WebDriverWait(driver, 10).until(
                ExpectedConditions.elementToBeClickable(
                        home.findElement(By.linkText("Help"))
                )
        );

        help.click();
        Assertions.assertEquals("Help", driver.getTitle());
    }


    @AfterEach
    public void closeBrowser(){
        SessionManager.quit(driver);
        testLog.stop();
    }
}
