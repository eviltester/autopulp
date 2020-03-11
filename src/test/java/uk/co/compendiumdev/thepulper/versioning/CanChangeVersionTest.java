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

    // There is a risk that the link urls don't work - check if this is the case

    @Test
    public void canChangeVersionViaUrlQueryParam(){

        for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){
            driver.get(url + "?v=" + version);

            assertFooterShowsCorrectVersion(version);
        }
    }

    @Test
    public void canChangeVersionViaDirectUrl(){

        // these direct urls are used by the other pages so if we check here
        // all we do next time is check that the links are enabled, clickable
        // and have the correct href
        for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){
            //https://thepulper.herokuapp.com/apps/pulp/gui/admin/version/10
            driver.get(url + "gui/admin/version/" + version);

            assertFooterShowsCorrectVersion(version);
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

    // there is a risk that the links on the page do not use the correct
    // change urls and might not be clickable - check if this is the case
    @Test
    public void canChangeVersionViaHelpPage(){

        for(int getversion = 1; getversion <= ThePulperApp.MAXVERSION; getversion++) {

            driver.get(url + "gui/help" + "?v=" + getversion);

            for (int version = 1; version <= ThePulperApp.MAXVERSION; version++) {

                final WebElement link =
                        new WebDriverWait(driver, 10).until(
                                ExpectedConditions.elementToBeClickable(
                                        By.cssSelector("#help-list-set-version-" + version + " a")
                                )
                        );

                assertLinkHasCorrectStateForVersion(link, version);
            }
        }
    }

    @Test
    public void canChangeVersionViaAdminPage(){

        for(int getversion = 1; getversion <= ThePulperApp.MAXVERSION; getversion++) {

            driver.get(url + "/gui/menu/admin" + "?v=" + getversion);

            for (int version = 1; version <= ThePulperApp.MAXVERSION; version++) {

                final WebElement link =
                        driver.findElement(By.cssSelector("#help-list-set-version-" + version + " a"));

                assertLinkHasCorrectStateForVersion(link, version);
            }
        }
    }

    @Test
    public void canChangeVersionViaAdminMenu(){

        for(int getversion = 1; getversion <= ThePulperApp.MAXVERSION; getversion++) {

            driver.get(url+ "?v=" + getversion);

            for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){

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

                assertLinkHasCorrectStateForVersion(link, version);
            }
        }
    }

    private void assertLinkHasCorrectStateForVersion(final WebElement link, final int version) {
        Assertions.assertTrue(link.isDisplayed(),
                "link for version "+ version + " not displayed");
        Assertions.assertTrue(link.isEnabled(),
                "link for version "+ version + " not enabled");
        Assertions.assertEquals(
                url + "gui/admin/version/" + version,
                link.getAttribute("href"),
                "link for version "+ version + " not admin version change as expected");
    }


    @AfterEach
    public void closeBrowser(){
        driver.close();
    }
}
