package com.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DemoTest {
    @Test
    public void openGoogle() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");

        // Wait for 3 seconds to hold the page
        Thread.sleep(3000);

        Assert.assertEquals(driver.getTitle(), "Google");
        driver.quit();
    }
}
