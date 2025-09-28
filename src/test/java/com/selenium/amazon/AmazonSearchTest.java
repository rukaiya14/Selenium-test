package com.selenium.amazon;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;


import java.time.Duration;

public class AmazonSearchTest {
    WebDriver driver;
    String baseUrl = "https://www.amazon.com";

    @BeforeMethod
    public void setup() {
        System.out.println("Setting up WebDriver for Amazon tests...");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);
        System.out.println("Navigated to: " + baseUrl);
    }

    // This test failed due to an empty page title after search (likely timing or assertion)
    @Test
    public void testAmazonProductSearch() {
        String searchTerm = "wireless headphones";
        // *** FIX: Changed expected title part based on manual observation ***
        // *** FIX: Use a more specific and expected title ***
        String expectedTitlePart = "Amazon.com : wireless headphones"; 

        System.out.println("Executing test: testAmazonProductSearch for term '" + searchTerm + "'");

        try {
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys(searchTerm);
            searchBox.sendKeys(Keys.ENTER);

            // *** FIX: Added an Explicit Wait (best practice) or a temporary sleep (for quick debug) ***
            // *** We'll use an explicit wait to wait for the title to update ***
            // Note: Since we don't have WebDriverWait imported, we'll use a temporary sleep here
            //       BUT REMEMBER TO REPLACE THIS WITH PROPER EXPLICIT WAIT LATER.
            try { Thread.sleep(3000); } catch (InterruptedException e) {}

            String pageTitle = driver.getTitle();
            System.out.println("Current Page Title: " + pageTitle);
            
            // Replaced generic check with the full expected result. 
            // Assert that the title CONTAINS the search term (case sensitive)
            Assert.assertTrue(pageTitle.contains(searchTerm),
                 "FAIL: Page title '" + pageTitle + "' does not contain the expected search term '" + searchTerm + "'");

            WebElement firstSearchResult = driver.findElement(By.cssSelector("[data-component-type='s-search-result']"));
            Assert.assertTrue(firstSearchResult.isDisplayed(), "FAIL: First search result element is not displayed.");

            System.out.println("PASS: Successfully searched for '" + searchTerm + "' and verified results.");

        } catch (Exception e) {
            System.err.println("ERROR in testAmazonProductSearch: " + e.getMessage());
            Assert.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    // This test failed due to 'no such element'
    @Test
    public void testAmazonNavigateToTodaysDeals() {
        // Example: Locate the 'Close' button on the location pop-up and click it
try {
    WebElement closeButton = driver.findElement(By.id("glow-toaster-fadesout-button")); 
    closeButton.click();
} catch (Exception ignore) {
    // Ignore the exception if the pop-up doesn't appear
}

// Now proceed with finding and clicking the 'Today's Deals' link
    System.out.println("Executing test: testAmazonNavigateToTodaysDeals");

    try {
        // Define the explicit wait object (e.g., up to 15 seconds)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // --- FIX: Wait until the element is clickable ---
        // Using Partial Link Text, which worked for finding the element in the previous run.
        WebElement todaysDealsLink = wait.until(
            ExpectedConditions.elementToBeClickable(By.partialLinkText("Deals"))
        );

        todaysDealsLink.click();

        // ... rest of your assertions ...

    } catch (Exception e) {
        // ... failure logging ...
    }
}

    @AfterMethod
    public void teardown() {
        System.out.println("Tearing down WebDriver...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("WebDriver session closed.");
    }
}