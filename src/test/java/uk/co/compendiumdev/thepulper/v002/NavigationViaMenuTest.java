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
        Created a copy of the v001 navigation test and
        will do the same for v 3 and then compare these to identify
        the common patterns for automating
     */
    @Test
    public void canNavigateAroundSiteUsingMenu(){

        driver.get(url + "?v=2");

        int totalMenuItems = 37;

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version 2"
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
        menuXpageTitle.put("Create", "Create Menu");
        menuXpageTitle.put("Reports", "Reports Menu");
        menuXpageTitle.put("Admin", "Admin Menu");

        // sub menus
        menuXpageTitle.put("Home > Help", "Help");
        menuXpageTitle.put("Home > Menu", "Pulp App Main Menu");
        menuXpageTitle.put("Books > Table", "Table of Books");
        menuXpageTitle.put("Books > List", "List of Books");
        menuXpageTitle.put("Authors > List", "List of Authors");
        menuXpageTitle.put("Authors > FAQ", "List of Authors");
        menuXpageTitle.put("Create > Author", "Create Author");
        menuXpageTitle.put("Create > Series", "Create Series");
        menuXpageTitle.put("Create > Publisher", "Create Publisher");
        menuXpageTitle.put("Create > Book", "Create Book");
        menuXpageTitle.put("Reports > Books", "Table of Books");
        menuXpageTitle.put("Reports > Book List", "List of Books");
        menuXpageTitle.put("Reports > Authors", "List of Authors");
        menuXpageTitle.put("Reports > Publishers", "List of Publishers");
        menuXpageTitle.put("Reports > Series", "List of Series");
        menuXpageTitle.put("Reports > Years", "List of Years");
        menuXpageTitle.put("Admin > Filter", "Filter Test Page");

        int menusUsed = 0;

        for(String menuTitle : menuXpageTitle.keySet()){

            System.out.println(menuTitle);

            WebElement navUl = driver.findElement(By.cssSelector("#primary_nav_wrap ul"));

            String topMenuName = "";
            String subMenuName = "";
            String menuToNameClick = "";

            if(menuTitle.contains(">")){
                final String[] menuItems = menuTitle.split(" > ");
                topMenuName = menuItems[0];
                subMenuName = menuItems[1];
                menuToNameClick = subMenuName;
            }else{
                topMenuName = menuTitle;
                menuToNameClick = topMenuName;
            }

            if(subMenuName.length()!=0) {

                // hover on menu item
                WebElement topLevelMenu =
                        new WebDriverWait(driver,10).until(
                                ExpectedConditions.elementToBeClickable(
                                        By.linkText(topMenuName)
                                )
                        );

                // hover
                new Actions(driver).moveToElement(topLevelMenu).perform();

                // find the parent li
                topLevelMenu = topLevelMenu.findElement(By.xpath(".."));

                // then click sub menu item
                final WebElement clickOn =
                        new WebDriverWait(driver, 10).until(
                                ExpectedConditions.elementToBeClickable(
                                        topLevelMenu.findElement(
                                                By.linkText(menuToNameClick)
                                        )
                                )
                        );

                clickOn.click();

            }else {

                // click on menu item
                final WebElement clickOn =
                        new WebDriverWait(driver, 10).until(
                                ExpectedConditions.elementToBeClickable(
                                        By.linkText(menuToNameClick)
                                )
                        );

                clickOn.click();
            }

            // wait for page to load by checking title
            new WebDriverWait(driver, 10).until(
                    ExpectedConditions.titleIs(menuXpageTitle.get(menuTitle))
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
