package uk.co.compendiumdev.thepulper.v002;

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
import uk.co.compendiumdev.thepulper.v003.PulperDropDownMenuItem;
import uk.co.compendiumdev.thepulper.v003.PulperNavMenu;

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

    @Test
    public void canNavigateAroundSiteUsingMenuAbstraction(){

        driver.get(url + "?v=2");

        PulperNavMenu menu = new PulperNavMenu().getForVersion(2);

        int totalMenuItems = menu.countMenuItems();

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version 2"
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
