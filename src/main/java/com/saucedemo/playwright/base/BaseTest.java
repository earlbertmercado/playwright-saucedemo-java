package com.saucedemo.playwright.base;

import com.saucedemo.playwright.browser.BrowserManager;
import com.saucedemo.playwright.utils.InitializeProperties;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.Properties;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected BrowserManager browserManager;
    protected Page page;
    protected Properties properties;

    //  Initializes BrowserManager, loads properties, and launches the browser before any tests.
    @BeforeClass
    public void setupBrowserAndPages() {
        browserManager = new BrowserManager();
        properties = InitializeProperties.loadProperties();

        page = browserManager.initializeBrowser(properties);

        if (page == null) {
            throw new RuntimeException("Setup failed: Browser not launched.");
        }

        logger.info("Browser launched successfully.");
    }

    // Logs the current URL after each test method.
    @AfterMethod
    public void logCurrentUrl() {
        if (page != null) {
            logger.info("Current URL: {}", page.url());
        } else {
            logger.warn("Page is not initialized, cannot display URL.");
        }
    }

    //  Closes the browser and cleans up resources after all tests in the class have executed.
    @AfterClass
    public void tearDownBrowser() {
        if (browserManager != null) {
            browserManager.closeBrowser();
            logger.info("Browser closed successfully.");
        }
    }
}
