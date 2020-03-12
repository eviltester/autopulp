package uk.co.compendiumdev.thepulper.sessions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.compendiumdev.thepulper.abstractions.AppEnvironment;
import uk.co.compendiumdev.thepulper.abstractions.SessionManager;


public class CanReuseSessions {

    private WebDriver driver;


    @Test
    public void checkSessionOnAdminPage(){

        driver = SessionManager.getDriver();
        driver.get(AppEnvironment.baseUrl() + "/gui/menu/admin?v=10");
        final String session1apikey = driver.findElement(By.id("apikeyvalue")).getText();
        driver.close();

        driver = SessionManager.getDriver();
        driver.get(AppEnvironment.baseUrl() + "/gui/menu/admin?v=10");
        final String session2apikey = driver.findElement(By.id("apikeyvalue")).getText();
        driver.close();

        Assertions.assertEquals(session1apikey, session2apikey);
    }



    @AfterEach
    public void closeBrowser(){
        try {
            driver.close();
        }catch (Exception e){
            // probably closed already, don't really expect this test to fail
        }
    }
}
