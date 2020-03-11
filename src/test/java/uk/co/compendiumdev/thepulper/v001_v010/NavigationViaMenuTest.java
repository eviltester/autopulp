package uk.co.compendiumdev.thepulper.v001_v010;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.v003.PulperDropDownMenuItem;
import uk.co.compendiumdev.thepulper.v003.PulperNavMenu;

public class NavigationViaMenuTest {

    private String url;
    private WebDriver driver;

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        driver = new ChromeDriver();
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
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 , 4, 5, 6, 7, 8, 9, 10})
    public void canNavigateAroundSiteUsingMenuAbstractionForVersion(int version){

        driver.get(url + "?v=" + version);

        PulperNavMenu menu = new PulperNavMenu().getForVersion(version);

        int totalMenuItems = menu.countMenuItems();

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version "+version
        );


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
        driver.close();
    }

}
