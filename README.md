# Selenium Demo Tests

This repository contains Selenium TestNG automation examples for:

1. Opening a browser and navigating to Google.
2. Performing automatic calculation using an online scientific calculator.
3. Searching for products on Amazon India and fetching product titles.

---

## Dependencies

- Java JDK (version 21 or compatible)
- Chrome browser
- ChromeDriver [Click here to Download ChromeDriver](https://developer.chrome.com/docs/chromedriver/downloads)
- Add TestNG to the project dependencies
- Add Selenium WebDriver to the project dependencies

---


## Test 1: Open Browser and Navigate to Google
 
This test opens Chrome, navigates to Google, waits for 3 seconds, verifies the page title, and then closes the browser.  

```java
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

        // Verify page title
        Assert.assertEquals(driver.getTitle(), "Google");

        // Close the browser
        driver.quit();
    }
}
```

## Test 2: Auto Calculation Using Online Scientific Calculator
  
This test performs the subtraction operation `9 - 4` using the TCS iON Scientific Calculator, logs the result in TestNG reports, and asserts the result.

```java
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
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.tcsion.com/OnlineAssessment/ScientificCalculator/Calculator.html");
    }

    @Test
    public void testSubtraction() throws InterruptedException {
        
        // Perform 9 - 4 with small hold times
        driver.findElement(By.id("keyPad_btn9")).click();
        Thread.sleep(300); 
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
```

## Test 3: Amazon Product Search

Searches for a product on amazon.com and fetches the first 5 product titles. Includes small holds after operations and a 5-second pause before closing the browser.

```java
package com.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class AmazonSearchTest {

    @Test
    public void searchAmazonProduct() {
        // Set ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // Open Amazon India
            driver.get("https://www.amazon.in/");

            // Wait a bit for page to load
            Thread.sleep(1000);

            // Locate the search box
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            Thread.sleep(300);

            // Type the search query and press ENTER
            searchBox.sendKeys("laptop");
            searchBox.sendKeys(Keys.ENTER);

            // Explicit wait for search results
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.s-main-slot h2")));

            // Get first 5 product titles
            List<WebElement> products = driver.findElements(By.cssSelector("div.s-main-slot h2 span"));
            int count = Math.min(products.size(), 5);

            for (int i = 0; i < count; i++) {
                String title = products.get(i).getText();
                System.out.println("Product " + (i + 1) + ": " + title);
                Reporter.log("Product " + (i + 1) + ": " + title, true);
            }

            Thread.sleep(1000); // small hold before closing

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
```

## Notes

- Use Thread.sleep() for small hold times to allow UI updates between operations.
- Ensure all element IDs and CSS selectors match the current website versions.
- Results are logged to both console and TestNG reports. 

---

## Author

- Vedant Konde
