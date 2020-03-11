package uk.co.compendiumdev.thepulper.versioning;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;

public class CanChangeVersionTest {

    private String url;
    private WebDriver driver;

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        driver = new ChromeDriver();
    }

    @Test
    public void canChangeVersionViaUrlQueryParam(){

        for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){
            driver.get(url + "?v=" + version);

            assertFooterShowsCorrectVersion(version);
        }
    }

    @Test
    public void canChangeVersionViaHelpPage(){

        for(int getversion = 1; getversion <= ThePulperApp.MAXVERSION; getversion++) {

            for (int version = 1; version <= ThePulperApp.MAXVERSION; version++) {

                driver.get(url + "gui/help" + "?v=" + getversion);

                final WebElement link =
                        new WebDriverWait(driver, 10).until(
                                ExpectedConditions.elementToBeClickable(
                                        By.cssSelector("#help-list-set-version-" + version + " a")
                                )
                        );

                link.click();

                new WebDriverWait(driver, 10).until(
                        ExpectedConditions.urlToBe(AppEnvironment.baseUrl() + "gui/"));

                assertFooterShowsCorrectVersion(version);
            }
        }
    }

    @Test
    public void canChangeVersionViaAdminPage(){

        for(int getversion = 1; getversion <= ThePulperApp.MAXVERSION; getversion++) {

            for (int version = 1; version <= ThePulperApp.MAXVERSION; version++) {

                driver.get(url + "/gui/menu/admin" + "?v=" + getversion);

                final WebElement link =
                        driver.findElement(By.cssSelector("#help-list-set-version-" + version + " a"));

                link.click();

                new WebDriverWait(driver, 10).until(
                        ExpectedConditions.urlToBe(AppEnvironment.baseUrl() + "gui/"));

                assertFooterShowsCorrectVersion(version);
            }
        }
    }

    @Test
    public void canChangeVersionViaAdminMenu(){

        for(int getversion = 1; getversion <= ThePulperApp.MAXVERSION; getversion++) {

            for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){

                driver.get(url+ "?v=" + getversion);

                // id is not present in all versions - added in v3
                //final By adminMenuLocation = By.id("menu-admin-menu");
                final By adminMenuLocation = By.linkText("Admin");

                final WebElement adminMenu =
                        new WebDriverWait(driver,10).until(
                        ExpectedConditions.elementToBeClickable(adminMenuLocation));


                // hover
                new Actions(driver).moveToElement(adminMenu).perform();



                final WebElement link =
                        driver.findElement(By.cssSelector("#menu-set-version-" + version + " a"));

                link.click();

                new WebDriverWait(driver,10).until(
                        ExpectedConditions.urlToBe(AppEnvironment.baseUrl()+"gui/"));

                assertFooterShowsCorrectVersion(version);
            }
        }
    }

    private void assertFooterShowsCorrectVersion(final int version) {
        final WebElement footer = driver.findElement(By.className("footer"));
        final String expectedVersionRender = String.format("v%03d", version);
        Assertions.assertEquals(
                "version " + expectedVersionRender,
                footer.getText().trim().substring(0, 12),
                String.format("Version %d does not render as %s", version, expectedVersionRender)
        );
    }

    @AfterEach
    public void closeBrowser(){
        driver.close();
    }
}
