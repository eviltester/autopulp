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
        Created a copy of the v001 navigation test and
        will do the same for v 3 and then compare these to identify
        the common patterns for automating
     */
    @Test
    public void canNavigateAroundSiteUsingMenu(){

        driver.get(url + "?v=3");

        int totalMenuItems = 44;

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version 3"
        );


        // Top level menus
        Map<String, String> menuXpageTitle = new HashMap<>();

        menuXpageTitle.put("Home", "Pulp App Main Menu");
        menuXpageTitle.put("Books", "Books Menu");
        menuXpageTitle.put("Authors", "Authors Menu");
        menuXpageTitle.put("Publishers", "Publishers Menu");
        menuXpageTitle.put("Series", "Series Menu");
        menuXpageTitle.put("Years", "Years Menu");
        menuXpageTitle.put("Search", "Search Page");
        menuXpageTitle.put("Create", "Create Menu");
        menuXpageTitle.put("Reports", "Reports Menu");
        menuXpageTitle.put("Admin", "Admin Menu");

        // sub menus
        menuXpageTitle.put("Home > Help", "Help");
        menuXpageTitle.put("Home > Menu", "Pulp App Main Menu");
        menuXpageTitle.put("Books > Table", "Table of Books");
        menuXpageTitle.put("Books > List", "List of Books");
        menuXpageTitle.put("Books > FAQs", "List of Books");
        menuXpageTitle.put("Authors > List", "List of Authors");
        menuXpageTitle.put("Authors > FAQ", "List of Authors");
        menuXpageTitle.put("Publishers > Publishers", "List of Publishers");
        menuXpageTitle.put("Publishers > FAQ", "List of Publishers");
        menuXpageTitle.put("Series > Series", "List of Series");
        menuXpageTitle.put("Series > FAQ", "List of Series");
        menuXpageTitle.put("Years > Years", "List of Years");
        menuXpageTitle.put("Years > FAQs", "List of Years");

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

                // find the parent li sub ul
                topLevelMenu = topLevelMenu.findElement(By.xpath("../ul"));

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

    /*
        Instead of just a page title I created an object to represent the menu item

        And this test has the basic code to use it.

        I will compare this with the code above and refactor it into a 'PulperNavMenu' object

        I'm not sure how this will handle versioning - normally that would not be an issue
        and I would simple version the code for each app versions. But they all need to live
        together in this code base, I suspect some sort of 'factory' will be used to return
        the menu object for each version.
     */
    @Test
    public void canNavigateAroundSiteUsingMenuWithIds(){

        driver.get(url + "?v=3");

        int totalMenuItems = 44;

        Assertions.assertEquals(totalMenuItems,
                driver.findElements(
                        By.cssSelector("#primary_nav_wrap ul li")).size(),
                "Unexpected number of menu items in version 3"
        );


        // Top level menus
        Map<String, PulperDropDownMenuItem> menuXpageTitle = new HashMap<>();

        menuXpageTitle.put("Home", new PulperDropDownMenuItem("menu-home", "Home", "Pulp App Main Menu"));
        menuXpageTitle.put("Books", new PulperDropDownMenuItem("menu-books-menu", "Books", "Books Menu"));
        menuXpageTitle.put("Authors", new PulperDropDownMenuItem("menu-authors-menu", "Authors", "Authors Menu"));
        menuXpageTitle.put("Publishers", new PulperDropDownMenuItem("menu-publishers-menu", "Publishers", "Publishers Menu"));
        menuXpageTitle.put("Series", new PulperDropDownMenuItem("menu-series-menu", "Series", "Series Menu"));
        menuXpageTitle.put("Years", new PulperDropDownMenuItem("menu-years-menu", "Years", "Years Menu"));
        menuXpageTitle.put("Search", new PulperDropDownMenuItem("menu-books-search", "Search", "Search Page"));
        menuXpageTitle.put("Create", new PulperDropDownMenuItem("menu-create-menu", "Create", "Create Menu"));
        menuXpageTitle.put("Reports", new PulperDropDownMenuItem("menu-reports-menu", "Reports", "Reports Menu"));
        menuXpageTitle.put("Admin", new PulperDropDownMenuItem("menu-admin-menu", "Admin", "Admin Menu"));

        // sub menus
        menuXpageTitle.put("Home > Help", new PulperDropDownMenuItem("menu-help", "Help", "Help"));
        menuXpageTitle.put("Home > Menu", new PulperDropDownMenuItem("submenu-home", "Menu", "Pulp App Main Menu"));
        menuXpageTitle.put("Books > Table", new PulperDropDownMenuItem("menu-books-table", "Table", "Table of Books"));
        menuXpageTitle.put("Books > List", new PulperDropDownMenuItem("menu-books-list", "List", "List of Books"));
        menuXpageTitle.put("Books > FAQs", new PulperDropDownMenuItem("menu-books-list-faq", "FAQs", "List of Books"));
        menuXpageTitle.put("Authors > List", new PulperDropDownMenuItem("menu-authors-list", "List", "List of Authors"));
        menuXpageTitle.put("Authors > FAQ", new PulperDropDownMenuItem("menu-authors-faq-list", "FAQ", "List of Authors"));
        menuXpageTitle.put("Publishers > Publishers", new PulperDropDownMenuItem("menu-publishers-list", "Publishers", "List of Publishers"));
        menuXpageTitle.put("Publishers > FAQ", new PulperDropDownMenuItem("menu-publishers-faq-list", "FAQ", "List of Publishers"));
        menuXpageTitle.put("Series > Series", new PulperDropDownMenuItem("menu-series-list", "Series", "List of Series"));
        menuXpageTitle.put("Series > FAQ", new PulperDropDownMenuItem("menu-series-faq-list", "FAQ", "List of Series"));
        menuXpageTitle.put("Years > Years", new PulperDropDownMenuItem("menu-years-list", "Years", "List of Years"));
        menuXpageTitle.put("Years > FAQs", new PulperDropDownMenuItem("menu-years-faq-list", "FAQs", "List of Years"));

        menuXpageTitle.put("Create > Author", new PulperDropDownMenuItem("menu-create-author", "Author", "Create Author"));
        menuXpageTitle.put("Create > Series", new PulperDropDownMenuItem("menu-create-series", "Series", "Create Series"));
        menuXpageTitle.put("Create > Publisher", new PulperDropDownMenuItem("menu-create-publisher", "Publisher", "Create Publisher"));
        menuXpageTitle.put("Create > Book", new PulperDropDownMenuItem("menu-create-book", "Book", "Create Book"));
        menuXpageTitle.put("Reports > Books", new PulperDropDownMenuItem("menu-books-report-table", "Books", "Table of Books"));
        menuXpageTitle.put("Reports > Book List", new PulperDropDownMenuItem("menu-books-report-list", "Book List", "List of Books"));
        menuXpageTitle.put("Reports > Authors", new PulperDropDownMenuItem("menu-authors-report-list", "Authors", "List of Authors"));
        menuXpageTitle.put("Reports > Publishers", new PulperDropDownMenuItem("menu-publishers-report-list", "Publishers", "List of Publishers"));
        menuXpageTitle.put("Reports > Series", new PulperDropDownMenuItem("menu-series-report-list", "Series", "List of Series"));
        menuXpageTitle.put("Reports > Years", new PulperDropDownMenuItem("menu-years-report-list", "Years", "List of Years"));
        menuXpageTitle.put("Admin > Filter", new PulperDropDownMenuItem("menu-admin-filter", "Filter", "Filter Test Page"));

        int menusUsed = 0;

        for(String menuTitle : menuXpageTitle.keySet()){

            System.out.println(menuTitle);

            String topMenuName = "";
            String subMenuName = "";

            if(menuTitle.contains(">")){
                final String[] menuItems = menuTitle.split(" > ");
                topMenuName = menuItems[0];
                subMenuName = menuItems[1];
            }else{
                topMenuName = menuTitle;
            }

            PulperDropDownMenuItem menuItemUsed;

            if(subMenuName.length()!=0) {

                // hover on menu item
                final PulperDropDownMenuItem menuItemToClick = menuXpageTitle.get(menuTitle);
                menuItemUsed = menuItemToClick;
                final PulperDropDownMenuItem parentMenuItem = menuXpageTitle.get(topMenuName);

                WebElement topLevelMenu =
                        new WebDriverWait(driver,10).until(
                                ExpectedConditions.elementToBeClickable(
                                        By.id(parentMenuItem.menuId())
                                )
                        );

                // hover
                new Actions(driver).moveToElement(topLevelMenu).perform();

                // find the parent li sub ul
                //topLevelMenu = topLevelMenu.findElement(By.xpath("../ul"));

                // then click sub menu item
                final WebElement clickOn =
                        new WebDriverWait(driver, 10).until(
                                ExpectedConditions.elementToBeClickable(
                                        topLevelMenu.findElement(
                                                By.id(menuItemToClick.menuId())
                                        )
                                )
                        );

                clickOn.click();

            }else {

                final PulperDropDownMenuItem menuItemToClick = menuXpageTitle.get(menuTitle);
                menuItemUsed = menuItemToClick;

                // click on menu item
                final WebElement clickOn =
                        new WebDriverWait(driver, 10).until(
                                ExpectedConditions.elementToBeClickable(
                                        By.id(menuItemToClick.menuId())
                                )
                        );

                clickOn.click();
            }

            // wait for page to load by checking title
            new WebDriverWait(driver, 10).until(
                    ExpectedConditions.titleIs(menuItemUsed.pageTitle())
            );

            menusUsed++;
        }

        Assertions.assertEquals(totalMenuItems, menusUsed + 10 /* 10 admin links */);
    }

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
