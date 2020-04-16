package uk.co.compendiumdev.thepulper.versioning;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;
import uk.co.compendiumdev.thepulper.abstractions.navigation.PulperNavMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
    This was the first test I wrote for The Pulper.

    When starting to automate, you can basically start anywhere
    because this forced me to create the pom.xml and the project.

    And the initial abstractions about environment will be used
    throughout the automated execution.

    This is a fairly non-standard test because most apps don't deal
    with multiple versions. And it is an exhaustive test simply because
    it seemed harder to write an allpairs or random sampling of version
    combinations than two nested loops for exhaustive coverage.

    Initially the links were all clicked on for all versions so this was
    slightly slower.

    Changed it to have two high risk mitigations for query version changing
    and url changing - these trigger clicks.

    Then the rest of the version link checking tests check the attributes of
    the links rather than clicking and following them.

    In theory these tests don't need to use a browser, they could use HTTP directly
    because the rendered HTML is not created by JavaScript.

    I haven't optimised this because... I can basically start anywhere and I will
    end up abstracting some of the code in these tests into abstractions anyway.

    I may well end up creating HTTP only tests to illustrate the point.

    Current risks with this:

    - time of test execution
        - could be shortened by using a single browser open for all tests
        - could be mitigated entirely by using HTTP only
    - multiple sessions
        - each browser open creates a new session on the test app, this might increase
          load on the server so probably want to find a session sharing mechanism
          either by sharing browser or by saving and injecting session for each new
          browser
    - only running on Chrome as haven't created a Driver abstraction yet
    - tests are very low level as haven't abstracted much yet, so could be maintenance
      overhead
 */
public class CanChangeVersionTest {

    private String url;
    private WebDriver driver;

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        driver = SessionManager.getDriver();
    }


    /*
        I started using this pattern in the NavigationViaMenuTest
        and it seemed to fit hear to iterate over all versions
        and avoid for loops in my test.
     */
    static IntStream allPulperVersions() {
        return IntStream.rangeClosed(1, ThePulperApp.MAXVERSION);
    }


    @DisplayName("Change the version using a url param and check footer OK")
    @ParameterizedTest(name = "using version {0} \"?v={0}\"")
    @MethodSource("allPulperVersions")
    public void canChangeVersionViaUrlQueryParam(int version){

            driver.get(url + "?v=" + version);
            assertFooterShowsCorrectVersion(version);
    }

    @DisplayName("Change the version by directly using the gui/admin/version url")
    @ParameterizedTest(name = "using version gui/admin/version/{0}")
    @MethodSource("allPulperVersions")
    public void canChangeVersionViaDirectUrl(int version){

        // these direct urls are used by the other pages so if we check here
        // all we do next time is check that the links are enabled, clickable
        // and have the correct href
        //https://thepulper.herokuapp.com/apps/pulp/gui/admin/version/10
        driver.get(url + "gui/admin/version/" + version);
        assertFooterShowsCorrectVersion(version);
    }

    private void assertFooterShowsCorrectVersion(final int version) {
        final WebElement footer = driver.findElement(By.className("footer"));
        final String expectedVersionRender = String.format("v%03d", version);
        Assertions.assertEquals(
                "version " + expectedVersionRender,
                footer.getText().trim().substring(0, 12),
                String.format("Version %d does not render as %s", version, expectedVersionRender)
        );
    }


    /*
        The following tests use a main version and then check for
        other versions

        At the moment I do this exhaustively because 10x10 is only 100
        but this parameterisation could do some sort of all pairs or
        sampling if we wanted to
     */

    static Stream pulperVersionCombos() {
        List<Arguments> args = new ArrayList<>();

        for(int getversion=1; getversion<= ThePulperApp.MAXVERSION; getversion++){
            for(int version=1; version<= ThePulperApp.MAXVERSION; version++){
                args.add(Arguments.of(getversion, version));
            }
        }
        return args.stream();
    }

    // there is a risk that the links on the page do not use the correct
    // change urls and might not be clickable - check if this is the case
    @DisplayName("Get Help Page and check href for all listed versions")
    @ParameterizedTest(name = "checking version help page {0} and version {1}")
    @MethodSource("pulperVersionCombos")
    public void canChangeVersionViaHelpPage(int getversion, int checkversion){

        driver.get(url + "gui/help" + "?v=" + getversion);

        new WebDriverWait(driver, 10).until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#help-list-set-version-1 a")
                )
        );

        final WebElement link = driver.findElement(By.cssSelector("#help-list-set-version-" + checkversion + " a"));
        assertLinkHasCorrectStateForVersion(link, checkversion);
    }


    @DisplayName("Get Admin page and check href for all listed versions")
    @ParameterizedTest(name = "checking version admin page {0} and version {1}")
    @MethodSource("pulperVersionCombos")
    public void canChangeVersionViaAdminPage(int getversion, int version){

            driver.get(url + "/gui/menu/admin" + "?v=" + getversion);

            final WebElement link =
                    driver.findElement(By.cssSelector("#help-list-set-version-" + version + " a"));

            assertLinkHasCorrectStateForVersion(link, version);
    }

    @DisplayName("Get base page fro version and check admin menu contains all versions")
    @ParameterizedTest(name = "checking version {0} and looking for version {1}")
    @MethodSource("pulperVersionCombos")
    public void canChangeVersionViaAdminMenu(int getversion, int version){

            driver.get(url+ "?v=" + getversion);

            final PulperNavMenu nav = new PulperNavMenu().getForVersion(getversion);
            nav.hoverMenuItem(driver, "Admin");

//            for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){

                final WebElement link =
                        driver.findElement(By.cssSelector("#menu-set-version-" + version + " a"));

                assertLinkHasCorrectStateForVersion(link, version);
 //           }
    }

    private void assertLinkHasCorrectStateForVersion(final WebElement link, final int version) {
        Assertions.assertTrue(link.isDisplayed(),
                "link for version "+ version + " not displayed");
        Assertions.assertTrue(link.isEnabled(),
                "link for version "+ version + " not enabled");
        Assertions.assertEquals(
                url + "gui/admin/version/" + version,
                link.getAttribute("href"),
                "link for version "+ version + " not admin version change as expected");
    }


    @AfterEach
    public void closeBrowser(){
        SessionManager.quit(driver);
    }
}
