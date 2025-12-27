package com.saucedemo.playwright.browser;

import com.saucedemo.playwright.constants.AppConstants;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class BrowserManager {

    private static final Logger logger = LogManager.getLogger(BrowserManager.class);

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static final ThreadLocal<BrowserManager> tlFactory = new ThreadLocal<>();

    public static BrowserManager getFactoryInstance() {
        return tlFactory.get();
    }

    public Page getPage() {
        return tlPage.get();
    }

    public Page initializeBrowser(Properties props) {

        // Resolve + log source
        String browserNameRaw = resolveConfig("browser", "CHROME", props);
        String headlessRaw = resolveConfig("isHeadless", "false", props);
        String widthRaw = resolveConfig("browserWidth", null, props);
        String heightRaw = resolveConfig("browserHeight", null, props);

        boolean headless = Boolean.parseBoolean(headlessRaw);

        Integer width = parseDimension(widthRaw, "width");
        Integer height = parseDimension(heightRaw, "height");

        BrowserTypes browserType = BrowserTypes.fromString(browserNameRaw);

        logger.info("Final Browser Configuration:");
        logger.info("Browser: {}", browserType);
        logger.info("Headless: {}", headless);
        logger.info("Width: {}", width);
        logger.info("Height: {}", height);

        tlPlaywright.set(Playwright.create());
        IBrowserCreator creator = BrowserFactory.createContext(browserType);

        tlBrowserContext.set(
                creator.createContext(tlPlaywright.get(), headless, width, height)
        );

        Page page = tlBrowserContext.get().newPage();
        page.navigate(AppConstants.BASE_URL);

        tlPage.set(page);
        tlFactory.set(this);

        logger.info("{} browser started successfully.", browserType);
        return page;
    }

    /**
     * Resolves a configuration key with explicit logging
     */
    private String resolveConfig(String key, String defaultValue, Properties props) {
        String sysValue = System.getProperty(key);

        if (sysValue != null) {
            logger.info("[OVERRIDE] {} picked from Jenkins/System property -> {}", key, sysValue);
            return sysValue;
        }

        String propValue = props.getProperty(key);

        if (propValue != null) {
            logger.info("[CONFIG] {} picked from config.properties -> {}", key, propValue);
            return propValue;
        }

        logger.info("[DEFAULT] {} using default -> {}", key, defaultValue);
        return defaultValue;
    }

    public void closeBrowser() {
        logger.info("Closing browser.");
        try {
            if (tlBrowserContext.get() != null) {
                Browser browser = tlBrowserContext.get().browser();
                tlBrowserContext.get().close();
                if (browser != null) browser.close();
            }
            if (tlPlaywright.get() != null) tlPlaywright.get().close();
        } catch (Exception e) {
            logger.error("Close failed: {}", e.getMessage(), e);
        } finally {
            tlPage.remove();
            tlBrowserContext.remove();
            tlPlaywright.remove();
            tlFactory.remove();
            logger.info("Cleanup done.");
        }
    }

    private Integer parseDimension(String value, String type) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                logger.warn("Invalid {} '{}'", type, value);
            }
        }
        return null;
    }
}
