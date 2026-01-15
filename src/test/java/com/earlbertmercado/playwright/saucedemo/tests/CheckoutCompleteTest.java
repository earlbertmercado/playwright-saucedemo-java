package com.earlbertmercado.playwright.saucedemo.tests;

import com.earlbertmercado.playwright.saucedemo.base.BaseTest;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.pages.CheckoutCompletePage;
import com.earlbertmercado.playwright.saucedemo.pages.InventoryPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutCompleteTest extends BaseTest {

    @Test
    public void testCheckoutCompletePageLoad() {
        final int FIRST_ITEM = 0;

        logger.info("Executing full checkout flow for item index: {}", FIRST_ITEM);
        CheckoutCompletePage checkoutCompletePage = loginPage
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(user.getFirstName())
                .enterLastName(user.getLastName())
                .enterPostalCode(user.getPostalCode())
                .clickContinueButton()
                .clickFinishButton();

        logger.info("Verifying final checkout completion state.");
        assertThat(page.url())
                .as("Checkout Complete page URL")
                .isEqualTo(AppConstants.CHECKOUT_COMPLETE_URL);

        softly.assertThat(checkoutCompletePage.getPageTitle())
                .as("Checkout Complete page title")
                .isEqualTo("Checkout: Complete!");

        softly.assertThat(checkoutCompletePage.isThankYouHeaderVisible())
                .as("Thank You header visible")
                .isTrue();

        softly.assertThat(checkoutCompletePage.isThankYouMessageVisible())
                .as("Thank You message visible")
                .isTrue();

        softly.assertThat(checkoutCompletePage.isBackHomeButtonVisible())
                .as("Back Home button visible")
                .isTrue();

        softly.assertAll();

        logger.debug("Cleaning up application state.");
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testBackHomeButton() {
        final int FIRST_ITEM = 0;

        logger.debug("Navigating through checkout to reach the Back Home button...");
        InventoryPage inventoryPage = loginPage
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(user.getFirstName())
                .enterLastName(user.getLastName())
                .enterPostalCode(user.getPostalCode())
                .clickContinueButton()
                .clickFinishButton()
                .clickBackHomeButton();

        logger.info("Verifying navigation back to Inventory Page.");
        assertThat(inventoryPage.getPage().url())
                .as("Navigated back to Inventory Page")
                .isEqualTo(AppConstants.INVENTORY_URL);

        appStateUtils.resetStateAndLogout();
    }
}
