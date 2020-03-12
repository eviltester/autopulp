package uk.co.compendiumdev.thepulper.allversions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;
import uk.co.compendiumdev.thepulper.abstractions.navigation.PulperDropDownMenuItem;
import uk.co.compendiumdev.thepulper.abstractions.navigation.PulperNavMenu;

import java.util.stream.IntStream;

public class NavigationViaMenuTest {

    private String url;
    private WebDriver driver;

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        driver = SessionManager.getDriver();
    }

    /*
        Since the PulperNavMenu is a model of the navigation menu.
        And is for each version.

        The JUnit5 functionality gives me the ability to cut down on
        the number of tests and use the model as a basis for automating against.

        I can use a parameterized test to check that the model
        matches the actual nav menu for each coded version.

        If this works then I can delete the individual
        NavigationViaMenuTest for each version.

        Originally this used a hard coded @ValueSource
        @ValueSource(ints = { 1, 2, 3 , 4, 5, 6, 7, 8, 9, 10})

        But that would require maintenance.

        Since I already have an abstraction for the max version.

        I can use that as the upper limit for a range so that this
        test automatically updates when a new version is added to the
        application and test code.

     */
    static IntStream allPulperVersions() {
        return IntStream.rangeClosed(1, ThePulperApp.MAXVERSION);
    }

    @ParameterizedTest
    @MethodSource("allPulperVersions")
    public void canNavigateAroundSiteUsingMenuAbstractionForVersion(int version){

        driver.get(url + "?v=" + version);

        PulperNavMenu menu = new PulperNavMenu().getForVersion(version);

        int totalMenuItems = menu.countMenuItems();

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version "+version
        );

        // check that we do use all the menu items
        int menusUsed = 0;

        for(String menuTitle : menu.itemKeys()){

            System.out.println(menuTitle);

            PulperDropDownMenuItem menuItemUsed;

            menuItemUsed = menu.clickMenuItem(driver, menuTitle);

            // wait for page to load by checking title
            new WebDriverWait(driver, 10).until(
                    ExpectedConditions.titleIs(menuItemUsed.pageTitle())
            );

            menusUsed++;
        }

        Assertions.assertEquals(totalMenuItems, menusUsed + 10 /* 10 admin links */);
    }

    @AfterEach
    public void closeBrowser(){
        SessionManager.quit(driver);
    }

}
