package com.earlbertmercado.playwright.saucedemo.base;

import com.earlbertmercado.playwright.saucedemo.browser.BrowserManager;
import com.earlbertmercado.playwright.saucedemo.dataprovider.TestDataLoader;
import com.earlbertmercado.playwright.saucedemo.dataprovider.TestDataUsers;
import com.earlbertmercado.playwright.saucedemo.pages.InventoryPage;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.earlbertmercado.playwright.saucedemo.pages.components.FooterComponent;
import com.earlbertmercado.playwright.saucedemo.pages.components.HeaderComponent;
import com.earlbertmercado.playwright.saucedemo.utils.AppStateUtils;
import com.earlbertmercado.playwright.saucedemo.utils.InitializeProperties;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Properties;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected BrowserManager browserManager;
    protected Page page;
    protected Properties properties;
    protected TestDataUsers user;
    protected AppStateUtils appStateUtils;
    protected SoftAssertions softly;

    protected HeaderComponent header;
    protected FooterComponent footer;
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;

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

    @BeforeMethod
    public void setupTestContext() {
        // Load User Data
        String userKey = System.getProperty("user", "standard_user");
        user = TestDataLoader.getUser(userKey);

        // Initialize Utilities
        this.appStateUtils = new AppStateUtils(page);
        this.softly = new SoftAssertions();
        this.header = new HeaderComponent(page);
        this.footer = new FooterComponent(page);
        this.loginPage = new LoginPage(page);
        this.inventoryPage = new InventoryPage(page);

        logger.info("Test context initialized for user: {}", userKey);
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
