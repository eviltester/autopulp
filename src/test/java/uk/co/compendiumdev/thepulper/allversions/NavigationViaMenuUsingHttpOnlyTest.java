package uk.co.compendiumdev.thepulper.allversions;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;
import uk.co.compendiumdev.thepulper.abstractions.navigation.PulperDropDownMenuItem;
import uk.co.compendiumdev.thepulper.abstractions.navigation.PulperNavMenu;
import uk.co.compendiumdev.thepulper.junitsupport.ConsoleTestLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
    This test didn't really need to be done using WebDriver.

    Compare with the WebDriver version to see the difference
 */
public class NavigationViaMenuUsingHttpOnlyTest {

    private String url;
    private WebDriver driver;
    private ConsoleTestLog testLog;

    @BeforeEach
    public void setupBrowser(TestInfo testinfo){
        url = AppEnvironment.baseUrl();
        driver = SessionManager.getDriver();
        testLog = new ConsoleTestLog(testinfo);
        testLog.start();
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

    // i want to make sure that my abstraction matches the actual nav
    // countMenuItems is the number of items in the menu
    // configuredNonAdminVersionItems is all the items that I would use
    // - admin version changing would use a different mechanism
    @DisplayName("Check model for nav matches number of items on nav")
    @ParameterizedTest(name = "using version {0}")
    @MethodSource("allPulperVersions")
    public void checkMenuItemsMatchModel(int version) {

        // this test doesn't really need webdriver, so use JSoup as the connection mechanism

        final Document document = SessionManager.jsoupConnectionGet(url + "?v=" + version);

        PulperNavMenu menu = new PulperNavMenu().getForVersion(version);

        Assertions.assertEquals(menu.countMenuItems(),
                    menu.countAdminMenuItems() + menu.configuredNonAdminVersionMenuItems());

        Assertions.assertEquals(menu.configuredNonAdminVersionMenuItems()+
                        menu.countAdminMenuItems(),
                document.select("#primary_nav_wrap ul li").size(),
                "Unexpected number of menu items in version " + version
        );
    }

    /*
        Rather than have loops in the test I thought I should
        experiment with the Paramaterized JUnit a bit more.

        I had to create a stream to feed in, and this seemed like
        an easy way to create a list first, then use it as a stream.
     */
    static Stream allPulperVersionsAndMenuItems() {

        // collate all the versions and all the key sets for that version

        List<Arguments> args = new ArrayList<>();

        for(int version=1; version<= ThePulperApp.MAXVERSION; version++){
            PulperNavMenu menu = new PulperNavMenu().getForVersion(version);

            for(String menuTitle : menu.itemKeys()){
                args.add(Arguments.of(version, menuTitle));
            }
        }

        return args.stream();
    }

    /*
        By using the 'name' and DisplayName I was able to avoid
        a System.out in the test because now the displayed
        test name has the information that I need to see what
        is being tested.
     */
    @DisplayName("Navigate Version Using Menu Abstraction")
    @ParameterizedTest(name = "using version {0} and following path \"{1}\"")
    @MethodSource("allPulperVersionsAndMenuItems")
    public void canNavigateAroundSiteUsingMenuAbstractionForVersion(int version, String menuPath){

        // assuming we can actually click on the menu then check that the urls lead where we expect
        // and we don't need WebDriver for that
        final Document document = SessionManager.jsoupConnectionGet(url + "?v=" + version);

        PulperNavMenu menu = new PulperNavMenu().getForVersion(version);

        PulperDropDownMenuItem menuItemUsed;

        menuItemUsed = menu.locateMenuItem(document, menuPath);

        final Document menuNavigatedPage = SessionManager.jsoupConnectionGet(AppEnvironment.domainUrl() + menuItemUsed.getUrl());
        Assertions.assertEquals(menuItemUsed.pageTitle(), menuNavigatedPage.title());

    }

    @AfterEach
    public void closeBrowser(){
        SessionManager.quit(driver);
        testLog.stop();
    }

}
