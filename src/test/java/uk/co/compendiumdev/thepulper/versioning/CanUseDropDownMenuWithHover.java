package uk.co.compendiumdev.thepulper.versioning;

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
import uk.co.compendiumdev.thepulper.abstractions.ThePulperApp;

import java.util.List;

/*
    Because my nav tests are using a 'fake hover' I wanted a test that actually
    does hover, and uses Action to do it, but this means I can't use the mouse when
    this is running.
 */
public class CanUseDropDownMenuWithHover {

    private String url;
    private WebDriver driver;

    @BeforeEach
    public void setupBrowser(){
        url = AppEnvironment.baseUrl();
        driver = new ChromeDriver();
    }

    @Test
    public void clickHelpOnAllVersionsAfterHover(){

        for(int version = 1; version <= ThePulperApp.MAXVERSION; version++){
            driver.get(url + "?v=" + version);
            WebElement home = driver.findElement(By.linkText("Home"));
            new Actions(driver).moveToElement(home).perform();
            home = home.findElement(By.xpath("..")); // get parent li
            final WebElement help = new WebDriverWait(driver, 10).until(
                    ExpectedConditions.elementToBeClickable(
                            home.findElement(By.linkText("Help"))
                    )
            );
            help.click();
            Assertions.assertEquals("Help", driver.getTitle());
        }
    }


    @AfterEach
    public void closeBrowser(){
        driver.close();
    }
}