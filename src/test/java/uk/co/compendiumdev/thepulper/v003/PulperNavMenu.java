package uk.co.compendiumdev.thepulper.v003;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PulperNavMenu {

    Map<String, PulperDropDownMenuItem> menuXDetails = new HashMap<>();

    public PulperNavMenu getForVersion(final int version) {

        createMenuForVersionThree();

        return this;
    }

    public int countMenuItems() {
        return 44;
    }

    public void createMenuForVersionThree(){

        // Top level menus
        menuXDetails = new HashMap<>();

        menuXDetails.put("Home", new PulperDropDownMenuItem("menu-home", "Home", "Pulp App Main Menu"));
        menuXDetails.put("Books", new PulperDropDownMenuItem("menu-books-menu", "Books", "Books Menu"));
        menuXDetails.put("Authors", new PulperDropDownMenuItem("menu-authors-menu", "Authors", "Authors Menu"));
        menuXDetails.put("Publishers", new PulperDropDownMenuItem("menu-publishers-menu", "Publishers", "Publishers Menu"));
        menuXDetails.put("Series", new PulperDropDownMenuItem("menu-series-menu", "Series", "Series Menu"));
        menuXDetails.put("Years", new PulperDropDownMenuItem("menu-years-menu", "Years", "Years Menu"));
        menuXDetails.put("Search", new PulperDropDownMenuItem("menu-books-search", "Search", "Search Page"));
        menuXDetails.put("Create", new PulperDropDownMenuItem("menu-create-menu", "Create", "Create Menu"));
        menuXDetails.put("Reports", new PulperDropDownMenuItem("menu-reports-menu", "Reports", "Reports Menu"));
        menuXDetails.put("Admin", new PulperDropDownMenuItem("menu-admin-menu", "Admin", "Admin Menu"));

        // sub menus
        menuXDetails.put("Home > Help", new PulperDropDownMenuItem("menu-help", "Help", "Help", "Home > Help"));
        menuXDetails.put("Home > Menu", new PulperDropDownMenuItem("submenu-home", "Menu", "Pulp App Main Menu", "Home > Menu"));
        menuXDetails.put("Books > Table", new PulperDropDownMenuItem("menu-books-table", "Table", "Table of Books", "Books > Table"));
        menuXDetails.put("Books > List", new PulperDropDownMenuItem("menu-books-list", "List", "List of Books", "Books > List"));
        menuXDetails.put("Books > FAQs", new PulperDropDownMenuItem("menu-books-list-faq", "FAQs", "List of Books", "Books > FAQs"));
        menuXDetails.put("Authors > List", new PulperDropDownMenuItem("menu-authors-list", "List", "List of Authors", "Authors > List"));
        menuXDetails.put("Authors > FAQ", new PulperDropDownMenuItem("menu-authors-faq-list", "FAQ", "List of Authors", "Authors > FAQ"));
        menuXDetails.put("Publishers > Publishers", new PulperDropDownMenuItem("menu-publishers-list", "Publishers", "List of Publishers", "Publishers > Publishers"));
        menuXDetails.put("Publishers > FAQ", new PulperDropDownMenuItem("menu-publishers-faq-list", "FAQ", "List of Publishers", "Publishers > FAQ"));
        menuXDetails.put("Series > Series", new PulperDropDownMenuItem("menu-series-list", "Series", "List of Series", "Series > Series"));
        menuXDetails.put("Series > FAQ", new PulperDropDownMenuItem("menu-series-faq-list", "FAQ", "List of Series", "Series > FAQ"));
        menuXDetails.put("Years > Years", new PulperDropDownMenuItem("menu-years-list", "Years", "List of Years", "Years > Years"));
        menuXDetails.put("Years > FAQs", new PulperDropDownMenuItem("menu-years-faq-list", "FAQs", "List of Years", "Years > FAQs"));

        menuXDetails.put("Create > Author", new PulperDropDownMenuItem("menu-create-author", "Author", "Create Author", "Create > Author"));
        menuXDetails.put("Create > Series", new PulperDropDownMenuItem("menu-create-series", "Series", "Create Series", "Create > Series"));
        menuXDetails.put("Create > Publisher", new PulperDropDownMenuItem("menu-create-publisher", "Publisher", "Create Publisher", "Create > Publisher"));
        menuXDetails.put("Create > Book", new PulperDropDownMenuItem("menu-create-book", "Book", "Create Book", "Create > Book"));
        menuXDetails.put("Reports > Books", new PulperDropDownMenuItem("menu-books-report-table", "Books", "Table of Books", "Reports > Books"));
        menuXDetails.put("Reports > Book List", new PulperDropDownMenuItem("menu-books-report-list", "Book List", "List of Books", "Reports > Book List"));
        menuXDetails.put("Reports > Authors", new PulperDropDownMenuItem("menu-authors-report-list", "Authors", "List of Authors", "Reports > Authors"));
        menuXDetails.put("Reports > Publishers", new PulperDropDownMenuItem("menu-publishers-report-list", "Publishers", "List of Publishers", "Reports > Publishers"));
        menuXDetails.put("Reports > Series", new PulperDropDownMenuItem("menu-series-report-list", "Series", "List of Series", "Reports > Series"));
        menuXDetails.put("Reports > Years", new PulperDropDownMenuItem("menu-years-report-list", "Years", "List of Years", "Reports > Years"));
        menuXDetails.put("Admin > Filter", new PulperDropDownMenuItem("menu-admin-filter", "Filter", "Filter Test Page", "Admin > Filter"));

    }

    public Set<String> itemKeys() {
        return menuXDetails.keySet();
    }

    public PulperDropDownMenuItem clickMenuItem(final WebDriver driver, final String menuTitle) {

        PulperDropDownMenuItem menuItem = menuXDetails.get(menuTitle);

        PulperDropDownMenuItem menuItemUsed;

        if(menuItem.isSubMenu()) {

            // hover on menu item
            final PulperDropDownMenuItem menuItemToClick = menuItem;
            menuItemUsed = menuItemToClick;
            final PulperDropDownMenuItem parentMenuItem = menuXDetails.get(menuItem.getParentTitle());

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

            final PulperDropDownMenuItem menuItemToClick = menuItem;
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

        return menuItemUsed;
    }
}
