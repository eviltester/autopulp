package uk.co.compendiumdev.thepulper.sessions;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;
import uk.co.compendiumdev.thepulper.junitsupport.ConsoleTestLog;


public class CanReuseSessionsTest {

    private WebDriver driver;
    private ConsoleTestLog testLog;

    @BeforeEach
    public void startTest(TestInfo testinfo){
        testLog = new ConsoleTestLog(testinfo);
        testLog.start();
    }

    String resetReuseSessionTo;
    @Test
    public void checkSessionOnAdminPage(){

        // force session manager to re-use session
        resetReuseSessionTo = System.getProperty("autopulp.reuseSession");

        String setReuseSessionTo = System.setProperty("autopulp.reuseSession", "true");

        driver = SessionManager.getUnmanagedDriver();
        driver.get(AppEnvironment.baseUrl() + "/gui/menu/admin?v=10");
        final String session1apikey = driver.findElement(By.id("apikeyvalue")).getText();
        driver.close();

        driver = SessionManager.getUnmanagedDriver();
        driver.get(AppEnvironment.baseUrl() + "/gui/menu/admin?v=10");
        final String session2apikey = driver.findElement(By.id("apikeyvalue")).getText();
        driver.close();

        Assertions.assertEquals(session1apikey, session2apikey);
    }



    @AfterEach
    public void closeBrowser(){

        if(resetReuseSessionTo!=null){
            System.setProperty("autopulp.reuseSession", resetReuseSessionTo);
        }

        try {
            SessionManager.quit(driver);
        }catch (Exception e){
            // probably closed already, don't really expect this test to fail
        }

        testLog.stop();
    }
}
