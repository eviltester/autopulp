package uk.co.compendiumdev.thepulper.v003;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;

import java.util.HashMap;
import java.util.Map;

public class NavigationViaMenuTest {

    private String url;
    private WebDriver driver;

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        driver = new ChromeDriver();
    }

    /*
        Using an abstraction layer for the navigation menu makes this
        a lot simpler.

        And making it versioned means that this test is almost exactly
        the same as the one for v2.

        A versioned abstraction can be harder to maintain though, but for
        this application it should be fine.

        Once I have this running for version 1 as well, I can start refactoring
        the PulperDropDownMenuItem class.
     */
    @Test
    public void canNavigateAroundSiteUsingMenuAbstraction(){

        driver.get(url + "?v=3");

        PulperNavMenu menu = new PulperNavMenu().getForVersion(3);

        int totalMenuItems = menu.countMenuItems();

        Assertions.assertEquals(44, totalMenuItems,
                "Unexpected number of menu items defined in version 3"
        );

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version 3"
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
