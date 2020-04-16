package uk.co.compendiumdev.thepulper.visual;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;

public class CanCheckVisualLayoutOfPagesForDifferentVersions {


    private ClassicRunner runner;
    private static BatchInfo batch;
    private String url;
    private WebDriver managedDriver;
    private WebDriver driver;
    private Eyes eyes;

    @BeforeAll
    public static void setupEyes(){
        batch = new BatchInfo("Test batch");

    }

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        runner = new ClassicRunner();
        eyes = new Eyes(runner);
        eyes.setApiKey("et1mRpqgS7nRI04ewMnL9Jei9qpugBHYKodmLnqJk0s110");
        eyes.setBatch(batch);
        managedDriver = SessionManager.getDriver();
        //driver = eyes.open(managedDriver); // if we don't add appname etc then test fails with no meaningful error message
    }

    @Test
    public void visuallyCompareDuringTesting(){


        driver = eyes.open(managedDriver,"The Pulper", "New Test", new RectangleSize(1200, 800));

        int version=2;

        driver.get(url + "?v=" + version);

        eyes.setForceFullPageScreenshot(true);
        eyes.checkWindow();

        // get this pointer away from nav - needed to get this working
        // reliably on Firefox
        new Actions(driver).moveToElement(
                driver.findElement(By.linkText("Contact"))).perform();

        WebElement home = driver.findElement(By.linkText("Home"));

        // hover on Home to drop menu down
        new Actions(driver).moveToElement(home).perform();
        home = home.findElement(By.xpath("..")); // get parent li

        eyes.checkWindow();

        // find Help
        final WebElement help = new WebDriverWait(driver, 10).until(
                ExpectedConditions.elementToBeClickable(
                        home.findElement(By.linkText("Help"))
                )
        );

        help.click();
        Assertions.assertEquals("Help", driver.getTitle());
        eyes.checkWindow();
    }


    @AfterEach
    public void closeBrowser(){
        eyes.closeAsync();
        SessionManager.quit(managedDriver);
    }
}
