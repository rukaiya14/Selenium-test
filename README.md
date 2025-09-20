YOU RUN ANY OF THE BELOW TESTS BY RUNNING THE CODE IN DemoTest.java FILE


------------------------------------------------------------------------------------------------
FOR OPENING BROWSERS:
-------------------------------------------------------------------------------------------------
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


--------------------------------------------------------------------------------------------------
FOR AUTO CALCULATION:
--------------------------------------------------------------------------------------------------
package com.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CalculatorTest {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        // Set ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\SeleniumDemo\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.tcsion.com/OnlineAssessment/ScientificCalculator/Calculator.html");
    }

    @Test
    public void testSubtraction() throws InterruptedException {
        
        // Perform 9 - 4 with small hold times
        driver.findElement(By.id("keyPad_btn9")).click();
        Thread.sleep(300); // hold 0.3 seconds
        driver.findElement(By.id("keyPad_btnMinus")).click();
        Thread.sleep(300);
        driver.findElement(By.id("keyPad_btn4")).click();
        Thread.sleep(300);
        driver.findElement(By.id("keyPad_btnEnter")).click();
        Thread.sleep(500); // wait for result to appear

        // Get result
        WebElement resultElement = driver.findElement(By.id("keyPad_UserInput"));
        String result = resultElement.getAttribute("value"); // get the display value

        // Show result in TestNG reports
        Reporter.log("Result of 9 - 4 is: " + result, true);

        // Assert result is correct
        Assert.assertEquals(result, "5", "Subtraction result is incorrect!");

        // Small hold after calculation
        Thread.sleep(1000);
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
