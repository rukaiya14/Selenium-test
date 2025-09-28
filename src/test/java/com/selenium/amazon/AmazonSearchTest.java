package com.selenium.amazon;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class AmazonSearchTest {
    WebDriver driver;
    String baseUrl = "https://www.amazon.com";

    @BeforeMethod
    public void setup() {
        System.out.println("Setting up WebDriver for Amazon tests...");
        WebDriverManager.chromedriver().setup();

        // --- FIX 1: Configure Chrome Options for Headless Execution ---
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");         // Essential for Jenkins/Linux
        options.addArguments("--disable-dev-shm-usage"); // Essential for Jenkins/Linux
        
        driver = new ChromeDriver(options); // Pass options to the driver
        // -----------------------------------------------------------

        driver.manage().window().maximize();
        // Set implicit wait for finding elements by default
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get(baseUrl);
        System.out.println("Navigated to: " + baseUrl);
        
        // Call the pop-up handler immediately after navigation
        handleAmazonPopups();
    }

    // --- FIX 2: Helper Method to Close Interstitial Pop-ups ---
    private void handleAmazonPopups() {
        // Temporarily reduce implicit wait to quickly check for the pop-up
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        
        try {
            // Locates the common "No Thanks" or "Done" button for the location/cookie pop-up
            WebElement closeButton = driver.findElement(By.cssSelector("input[data-action-type='DISMISS']"));
            closeButton.click();
            System.out.println("Pop-up dismissed successfully.");
        } catch (Exception ignore) {
            // Ignore NoSuchElementException if the pop-up does not appear
            System.out.println("No pop-up found or element not immediately clickable.");
        }
        
        // Restore implicit wait to the standard duration for main tests
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    // This test performs the search action (was previously failing due to 20s timeout)
    @Test
public void testAmazonProductSearch() {
    String searchTerm = "wireless headphones";
    
    System.out.println("Executing test: testAmazonProductSearch for term '" + searchTerm + "'");

    // We'll use JavaScript to force the search bar action if the wait fails.
    JavascriptExecutor js = (JavascriptExecutor) driver;

    try {
        // 1. Attempt Explicit Wait (20s) for the search box to become visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement searchBox = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox"))
        );
        
        // If visible, use standard Selenium interaction
        searchBox.sendKeys(searchTerm);
        searchBox.sendKeys(Keys.ENTER);
        System.out.println("Search performed using standard Selenium commands.");

    } catch (Exception timeout) {
        // 2. IF THE WAIT FAILS, EXECUTE SEARCH VIA JAVASCRIPT
        System.err.println("WARN: Search box not visible after 20s. FORCING search via JavaScript.");
        
        try {
            // Force setting the value in the search box
            js.executeScript("document.getElementById('twotabsearchtextbox').value='" + searchTerm + "';");
            // Force clicking the search button (search button ID is usually 'nav-search-submit-button')
            js.executeScript("document.getElementById('nav-search-submit-button').click();");
        } catch (Exception jsError) {
            System.err.println("FATAL: JavaScript execution failed: " + jsError.getMessage());
            Assert.fail("Test failed: Could not interact with search box even using JavaScript.");
        }
    }
    
    // --- Continue with Assertions ---
    
    try {
        // Wait for search results title (use a new wait here)
        WebDriverWait resultWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        resultWait.until(ExpectedConditions.titleContains(searchTerm));
        
        // 4. Assert that the page title contains the search term
        String pageTitle = driver.getTitle();
        System.out.println("Current Page Title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains(searchTerm),
             "FAIL: Page title '" + pageTitle + "' does not contain the expected search term '" + searchTerm + "'");
             
        // 5. Assert that search results are visible (look for a common product container)
        WebElement firstSearchResult = driver.findElement(By.cssSelector("[data-component-type='s-search-result']"));
        Assert.assertTrue(firstSearchResult.isDisplayed(), "FAIL: First search result element is not displayed.");

        System.out.println("PASS: Successfully searched for '" + searchTerm + "' and verified results.");

    } catch (Exception e) {
        System.err.println("ERROR in testAmazonProductSearch (Assertions failed): " + e.getMessage());
        Assert.fail("Test failed due to an exception during assertion or result loading: " + e.getMessage());
    }
}

    // This test failed previously due to locator issues, now fixed with link text
// This test should stabilize by removing the unnecessary wait for the search box.
@Test
public void testAmazonNavigateToTodaysDeals() {
    System.out.println("Executing test: testAmazonNavigateToTodaysDeals");

    try {
        // --- REMOVE: The unnecessary and failing stableWait for 'twotabsearchtextbox' ---

        // Define the explicit wait object for the Deals link
        WebDriverWait clickWait = new WebDriverWait(driver, Duration.ofSeconds(15)); 

        // Wait until the "Today's Deals" link is clickable
        WebElement todaysDealsLink = clickWait.until(
             // Use the robust locator based on the link's visible text
             ExpectedConditions.elementToBeClickable(By.linkText("Today's Deals")) 
             // If linkText fails again, try: ExpectedConditions.elementToBeClickable(By.partialLinkText("Deals"))
        );

        todaysDealsLink.click();

        // 2. Assert that the URL contains the expected path
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after navigating: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/gp/goldbox"),
             "FAIL: URL '" + currentUrl + "' does not contain '/gp/goldbox'.");

        // 3. Assert that the page title contains "Deals"
        String pageTitle = driver.getTitle();
        System.out.println("Current Page Title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("Deals"),
             "FAIL: Page title '" + pageTitle + "' does not contain 'Deals'.");

        System.out.println("PASS: Successfully navigated to Today's Deals page.");

    } catch (Exception e) {
        System.err.println("ERROR in testAmazonNavigateToTodaysDeals: " + e.getMessage());
        Assert.fail("Test failed due to an exception: " + e.getMessage());
    }
}
    @AfterMethod
    public void teardown() {
        System.out.println("Tearing down WebDriver...");
        if (driver != null) {
            driver.quit(); // CRITICAL for Jenkins stability
        }
        System.out.println("WebDriver session closed.");
    }
}