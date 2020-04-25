package uk.co.compendiumdev.thepulper.versioning;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;
import uk.co.compendiumdev.thepulper.junitsupport.ConsoleTestLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
    These tests don't really need WebDriver so converted to use HTTP directly
    using JSOUP - compare this with the WebDriver Version to see the difference
 */
public class CanChangeVersionUsingHttpOnlyTest {

    private String url;
    private WebDriver driver;
    private ConsoleTestLog testLog;

    @BeforeEach
    public void setupBrowser(TestInfo testinfo){
        url = AppEnvironment.baseUrl();
        //driver = SessionManager.getDriver();
        testLog = new ConsoleTestLog(testinfo);
        testLog.start();
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

        final Document document = SessionManager.jsoupConnectionGet(url + "?v=" + version);
        assertFooterShowsCorrectVersion(document, version);

    }


    @DisplayName("Change the version by directly using the gui/admin/version url")
    @ParameterizedTest(name = "using version gui/admin/version/{0}")
    @MethodSource("allPulperVersions")
    public void canChangeVersionViaDirectUrl(int version){

        final Document document = SessionManager.jsoupConnectionGet(url + "gui/admin/version/" + version);
        assertFooterShowsCorrectVersion(document, version);
    }

    private void assertFooterShowsCorrectVersion(final Document document, final int version) {
        final Element footer = document.select(".footer").get(0);
        final String expectedVersionRender = String.format("v%03d", version);
        Assertions.assertEquals(
                "version " + expectedVersionRender,
                footer.text().trim().substring(0, 12),
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

        // Since the help information is not populated by JavaScript then
        // we can use Jsoup directly

        final Document document = SessionManager.jsoupConnectionGet(url + "gui/help" + "?v=" + getversion);
        final Element link = document.selectFirst("#help-list-set-version-" + checkversion + " a");
        assertLinkHasCorrectStateForVersion(link, checkversion);
    }


    @DisplayName("Get Admin page and check href for all listed versions")
    @ParameterizedTest(name = "checking version admin page {0} and version {1}")
    @MethodSource("pulperVersionCombos")
    public void canChangeVersionViaAdminPage(int getversion, int version){

        final Document document = SessionManager.jsoupConnectionGet(url + "/gui/menu/admin" + "?v=" + getversion);
        final Element link = document.selectFirst("#help-list-set-version-" + version + " a");
        assertLinkHasCorrectStateForVersion(link, version);
    }

    @DisplayName("Get base page for version and check admin menu contains all versions")
    @ParameterizedTest(name = "checking version {0} and looking for version {1}")
    @MethodSource("pulperVersionCombos")
    public void canChangeVersionViaAdminMenu(int getversion, int version){

        // assuming we have tests that cover menu use using webdriver then we don't need to do this with WebDriver

        final Document document = SessionManager.jsoupConnectionGet(url+ "?v=" + getversion);
        final Element link = document.selectFirst("#menu-set-version-" + version + " a");
        assertLinkHasCorrectStateForVersion(link, version);

    }

    private void assertLinkHasCorrectStateForVersion(final Element link, final int version) {
        // NOTE: we lose the ability to check if displayed or enabled with JSoup
        // - TODO is there actually a risk that the link can not be clickable?
        Assertions.assertEquals(
                AppEnvironment.appRootPath() + "gui/admin/version/" + version,
                link.attr("href"),
                "link for version "+ version + " not admin version change as expected");
    }


    @AfterEach
    public void closeBrowser(){
        SessionManager.quit(driver);
        testLog.stop();
    }
}
