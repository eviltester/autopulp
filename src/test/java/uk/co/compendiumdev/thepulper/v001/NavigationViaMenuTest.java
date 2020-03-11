package uk.co.compendiumdev.thepulper.v001;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
        I am not sure how I am going to model the navigation so I
        will start with a simple set of hash maps

     */
    @Test
    public void canNavigateAroundSiteUsingMenu(){

        driver.get(url + "?v=1");


        Assertions.assertEquals(31,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version 1"
        );


        // Top level menus
        Map<String, String> menuXpageTitle = new HashMap<>();

        menuXpageTitle.put("Home", "Pulp App Main Menu");
        menuXpageTitle.put("Books", "Books Menu");
        menuXpageTitle.put("Authors", "Authors Menu");
        menuXpageTitle.put("Publishers", "List of Publishers");
        menuXpageTitle.put("Series", "List of Series");
        menuXpageTitle.put("Years", "List of Years");
        menuXpageTitle.put("Search", "Search Page");
        menuXpageTitle.put("Reports", "Reports Menu");
        menuXpageTitle.put("Admin", "Admin Menu");

        for(String menuTitle : menuXpageTitle.keySet()){

            WebElement navUl = driver.findElement(By.cssSelector("#primary_nav_wrap ul"));

            // click on menu item
            final WebElement clickOn =
                new WebDriverWait(driver, 10).until(
                        ExpectedConditions.elementToBeClickable(
                                By.linkText(menuTitle)
                        )
                );

            clickOn.click();

            // wait for page to load by checking title
            new WebDriverWait(driver, 10).until(
                    ExpectedConditions.titleIs(menuXpageTitle.get(menuTitle))
            );

        }




    }

    @AfterEach
    public void closeBrowser(){
        driver.close();
    }

}
