package com.saucedemo.playwright;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.pages.CheckoutCompletePage;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutCompleteTest extends BaseTest {

    @Test
    public void testCheckoutCompletePageLoad() {
        int FIRST_ITEM = 0;

        // Externalize test data later
        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutCompletePage checkoutCompletePage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode)
                .clickContinueButton()
                .clickFinishButton();

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
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testBackHomeButton() {
        int FIRST_ITEM = 0;

        // Externalize test data later
        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode)
                .clickContinueButton()
                .clickFinishButton()
                .clickBackHomeButton();

        assertThat(inventoryPage.getPage().url())
                .as("Navigated back to Inventory Page")
                .isEqualTo(AppConstants.INVENTORY_URL);

        appStateUtils.resetStateAndLogout();
    }
}
