package uk.co.compendiumdev.thepulper.v003;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

public class PulperNavMenu {

    Map<String, PulperDropDownMenuItem> menuXDetails = new HashMap<>();
    private int fullMenuItemCount; // including admin version switching

    public PulperNavMenu getForVersion(final int version) {

        switch (version){
            case 1:
                createMenuForVersionOne();
                break;
            case 2:
                createMenuForVersionTwo();
                break;
            case 3:
                createMenuForVersionThree();
                break;
            default:

        }

        return this;
    }

    public int countMenuItems() {
        return fullMenuItemCount;
    }

    public void createMenuForVersionOne(){

        fullMenuItemCount = 31;

        List<PulperDropDownMenuItem> menuItemsList = new ArrayList<>();

        menuItemsList.add(PulperDropDownMenuItem.withoutId("Home", "Pulp App Main Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Books", "Books Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Authors", "Authors Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Publishers", "List of Publishers"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Series", "List of Series"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Years", "List of Years"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Search", "Search Page"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Reports", "Reports Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Admin", "Admin Menu"));

        // sub menus
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Home > Help", "Help"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Home > Menu", "Pulp App Main Menu"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Books > Table", "Table of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Books > List", "List of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Authors > List", "List of Authors"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Authors > FAQ", "List of Authors"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Books", "Table of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Book List", "List of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Authors", "List of Authors"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Publishers", "List of Publishers"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Series", "List of Series"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Years", "List of Years"));

        addListAsMenuX(menuItemsList);

    }

    private void addListAsMenuX(final List<PulperDropDownMenuItem> menuItemsList) {
        menuXDetails = new HashMap<>();
        for(PulperDropDownMenuItem item : menuItemsList){
            menuXDetails.put(item.menuPath(), item);
        }
    }

    public void createMenuForVersionTwo(){
        fullMenuItemCount = 37;

        List<PulperDropDownMenuItem> menuItemsList = new ArrayList<>();

        menuItemsList.add(PulperDropDownMenuItem.withoutId("Home", "Pulp App Main Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Books", "Books Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Authors", "Authors Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Publishers", "List of Publishers"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Series", "List of Series"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Years", "List of Years"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Search", "Search Page"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Create", "Create Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Reports", "Reports Menu"));
        menuItemsList.add(PulperDropDownMenuItem.withoutId("Admin",  "Admin Menu"));

        // sub menus
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Home > Help", "Help"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Home > Menu", "Pulp App Main Menu"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Books > Table", "Table of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Books > List", "List of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Authors > List", "List of Authors"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Authors > FAQ", "List of Authors"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Create > Author", "Create Author"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Create > Series", "Create Series"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Create > Publisher", "Create Publisher"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Create > Book", "Create Book"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Books", "Table of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Book List", "List of Books"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Authors", "List of Authors"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Publishers", "List of Publishers"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Series", "List of Series"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Reports > Years", "List of Years"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithoutId("Admin > Filter", "Filter Test Page"));

        addListAsMenuX(menuItemsList);
    }

    public void createMenuForVersionThree(){

        fullMenuItemCount = 44;

        // Top level menus
        menuXDetails = new HashMap<>();

        List<PulperDropDownMenuItem> menuItemsList = new ArrayList<>();

        menuItemsList.add(PulperDropDownMenuItem.withId( "Home", "Pulp App Main Menu", "menu-home"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Books", "Books Menu", "menu-books-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Authors", "Authors Menu", "menu-authors-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Publishers", "Publishers Menu",  "menu-publishers-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Series", "Series Menu", "menu-series-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Years", "Years Menu", "menu-years-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Search", "Search Page", "menu-books-search"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Create", "Create Menu", "menu-create-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Reports", "Reports Menu", "menu-reports-menu"));
        menuItemsList.add(PulperDropDownMenuItem.withId( "Admin", "Admin Menu", "menu-admin-menu"));


        // sub menus
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Home > Help", "Help", "menu-help"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Home > Help", "Help", "menu-help"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Home > Menu", "Pulp App Main Menu", "submenu-home"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Books > Table", "Table of Books", "menu-books-table"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Books > List", "List of Books", "menu-books-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Books > FAQs", "List of Books", "menu-books-list-faq"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Authors > List", "List of Authors", "menu-authors-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Authors > FAQ", "List of Authors", "menu-authors-faq-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Publishers > Publishers", "List of Publishers", "menu-publishers-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Publishers > FAQ", "List of Publishers", "menu-publishers-faq-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Series > Series", "List of Series", "menu-series-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Series > FAQ", "List of Series", "menu-series-faq-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Years > Years", "List of Years", "menu-years-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Years > FAQs", "List of Years", "menu-years-faq-list"));

        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Create > Author", "Create Author", "menu-create-author"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Create > Series", "Create Series", "menu-create-series"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Create > Publisher", "Create Publisher", "menu-create-publisher"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Create > Book", "Create Book", "menu-create-book"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Reports > Books", "Table of Books", "menu-books-report-table"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Reports > Book List", "List of Books", "menu-books-report-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Reports > Authors", "List of Authors", "menu-authors-report-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Reports > Publishers", "List of Publishers", "menu-publishers-report-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Reports > Series", "List of Series", "menu-series-report-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Reports > Years", "List of Years", "menu-years-report-list"));
        menuItemsList.add(PulperDropDownMenuItem.subMenuItemWithId("Admin > Filter", "Filter Test Page", "menu-admin-filter"));

        addListAsMenuX(menuItemsList);
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

            By parentLocator = parentMenuItem.getLocator();

            WebElement topLevelMenu =
                    getMenuItemWhenClickable(driver, parentLocator);

            // fake hover by triggering alternative css - this prevents needing an action
            // simply because actions interfere with the machine so can't do anything else
            // NOTE: risk that hover doesn't actually work
            WebElement hoverOn = topLevelMenu;
            if(topLevelMenu.getTagName().equalsIgnoreCase("a")) {
                // if we found the anchor then locate the li above it
                hoverOn=topLevelMenu.findElement(By.xpath(".."));
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].className='shownow'", hoverOn);

            //real hover
            //new Actions(driver).moveToElement(topLevelMenu).perform();


            WebElement clickOn;
            SearchContext searchIn=driver;

            if(!menuItemToClick.hasId()) {
                // find the parent li sub ul
                topLevelMenu = topLevelMenu.findElement(By.xpath("../ul"));
                searchIn = topLevelMenu;
            }

            clickOn = findMenuItemWhenClickable(
                    searchIn, menuItemToClick.getLocator(), driver);

            clickOn.click();

        }else {

            final PulperDropDownMenuItem menuItemToClick = menuItem;
            menuItemUsed = menuItemToClick;

            // click on menu item
            getMenuItemWhenClickable(driver, menuItemToClick.getLocator()).click();

        }

        return menuItemUsed;
    }

    private WebElement findMenuItemWhenClickable(final SearchContext search, final By locator, final WebDriver driver) {
        return new WebDriverWait(driver, 10).until(
                ExpectedConditions.elementToBeClickable(
                        search.findElement(
                                locator
                        )
                )
        );
    }

    private WebElement getMenuItemWhenClickable(final WebDriver driver, final By parentLocator) {
        return new WebDriverWait(driver, 10).until(
                ExpectedConditions.elementToBeClickable(
                        parentLocator
                )
        );
    }
}
