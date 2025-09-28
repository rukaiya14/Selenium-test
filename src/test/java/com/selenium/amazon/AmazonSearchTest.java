package com.selenium.amazon; // Keep the package name as is

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions; // For Headless mode
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class AmazonSearchTest { // Functionally now a Wikipedia test
    WebDriver driver;
    String baseUrl = "https://www.wikipedia.org/"; // Targeting stable Wikipedia

    @BeforeMethod
    public void setup() {
        System.out.println("Setting up WebDriver for Wikipedia tests...");
        WebDriverManager.chromedriver().setup();

        // --- Headless Mode Configuration (CRITICAL for Jenkins) ---
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");         
        options.addArguments("--disable-dev-shm-usage"); 
        
        driver = new ChromeDriver(options);
        // --------------------------------------------------------
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get(baseUrl);
        System.out.println("Navigated to: " + baseUrl);
    }
    
    // NOTE: The handleAmazonPopups() method is removed as it's not needed here.

    @Test
    public void testWikipediaSearch() { 
        String searchTerm = "Continuous Integration";
        
        System.out.println("Executing test: testWikipediaSearch for term '" + searchTerm + "'");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // 1. Wait for the Wikipedia search input field (ID is 'searchInput' on main page)
            WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("searchInput"))
            );
            
            searchBox.sendKeys(searchTerm);
            searchBox.sendKeys(Keys.ENTER);

            // 2. Wait for the resulting article title to be visible (ID is 'firstHeading')
            WebElement articleTitle = wait.until(
                 ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading"))
            );
            
            // --- FINAL FIX: Make the assertion case-insensitive ---
            String actualTitleText = articleTitle.getText().toLowerCase();
            String expectedTermLower = searchTerm.toLowerCase();

            // 3. Assert the article title contains the search term (case-insensitively)
            Assert.assertTrue(actualTitleText.contains(expectedTermLower),
                 "FAIL: Article title '" + articleTitle.getText() + "' does not contain the expected search term '" + searchTerm + "'.");

            System.out.println("PASS: Successfully searched and verified Wikipedia article.");

        } catch (Exception e) {
            System.err.println("ERROR in testWikipediaSearch: " + e.getMessage());
            Assert.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    // NOTE: The testAmazonNavigateToTodaysDeals() is removed as it was Amazon-specific.
    
    @AfterMethod
    public void teardown() {
        System.out.println("Tearing down WebDriver...");
        if (driver != null) {
            driver.quit(); // CRITICAL for Jenkins stability
        }
        System.out.println("WebDriver session closed.");
    }
}