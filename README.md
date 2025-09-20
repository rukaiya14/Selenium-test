# Selenium Demo Tests

This repository contains Selenium TestNG automation examples for:

1. Opening a browser and navigating to Google.
2. Performing automatic calculation using an online scientific calculator.

---

## Prerequisites

- Java JDK installed (version 21 or compatible)
- Chrome browser installed
- ChromeDriver downloaded and path set in the test files [Download ChromeDriver](https://developer.chrome.com/docs/chromedriver/downloads)
- TestNG added to the project dependencies
- Selenium WebDriver added to the project dependencies

---


## Test 1: Open Browser and Navigate to Google

**File:** `DemoTest.java`  
**Description:** This test opens Chrome, navigates to Google, waits for 3 seconds, verifies the page title, and then closes the browser.  

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

**Steps to Run:**

1. Ensure `chromedriver.exe` is in your system PATH or provide the full path in your code.  
2. Run this test via TestNG or your IDE.  
3. The console will display the test results.

---

## Test 2: Auto Calculation Using Online Scientific Calculator

**File:** `CalculatorTest.java`  
**Description:** This test performs the subtraction operation `9 - 4` using the TCS iON Scientific Calculator, logs the result in TestNG reports, and asserts the result.

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

**Steps to Run:**

1. Ensure the ChromeDriver path matches your system path.  
2. Run the test via TestNG or your IDE.  
3. The result of the calculation will appear in the TestNG report and console.  

---

## Notes

- Use `Thread.sleep()` to add small hold times to allow the UI to update between button clicks.  
- Make sure the IDs for calculator buttons (`keyPad_btn9`, `keyPad_btnMinus`, etc.) match the current calculator page.  
- You can extend the calculator test for other operations by clicking the respective buttons and updating the expected result.  

---

## Author

- Vedant Konde
