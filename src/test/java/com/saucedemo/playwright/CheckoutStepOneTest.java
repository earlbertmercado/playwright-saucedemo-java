package com.saucedemo.playwright;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.constants.CheckoutStepOnePageLocators;
import com.saucedemo.playwright.pages.CheckoutStepOnePage;
import com.saucedemo.playwright.pages.CheckoutStepTwoPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepOneTest extends BaseTest {

    @Test
    public void testCheckoutStepOnePageLoad() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepOnePage checkoutStepOnePage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout();

        assertThat(page.url())
                .as("Checkout Step One page URL")
                .isEqualTo(AppConstants.CHECKOUT_STEP_ONE_URL);

        softly.assertThat(checkoutStepOnePage.getPageTitle())
                .as("Checkout Step One page title")
                .isEqualTo("Checkout: Your Information");

        softly.assertThat(checkoutStepOnePage.isFirstNameInputVisible())
                .as("First Name input visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isLastNameInputVisible())
                .as("Last Name input visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isPostalCodeInputVisible())
                .as("Postal Code input visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isContinueButtonVisible())
                .as("Continue button visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isCancelButtonVisible())
                .as("Cancel button visible")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneFormSubmission() {
        int FIRST_ITEM = 0;

        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepTwoPage checkoutStepTwoPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode)
                .clickContinueButton();

        // TODO: Add assertions for Checkout Step Two page
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneWithEmptyFirstName() {
        int FIRST_ITEM = 0;

        String lastName = "Doe";
        String postalCode = "12345";
        String expectedErrorMessage = "Error: First Name is required";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterLastName(lastName)
                .enterPostalCode(postalCode)
                .clickContinueButton();

        String actualErrorMessage = page
                .locator(CheckoutStepOnePageLocators.ERROR_MESSAGE)
                .textContent()
                .trim();

        assertThat(actualErrorMessage)
                .as("Error message for empty First Name")
                .isEqualTo(expectedErrorMessage);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneWithEmptyLastName() {
        int FIRST_ITEM = 0;

        String firstName = "John";
        String postalCode = "12345";
        String expectedErrorMessage = "Error: Last Name is required";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(firstName)
                .enterPostalCode(postalCode)
                .clickContinueButton();

        String actualErrorMessage = page
                .locator(CheckoutStepOnePageLocators.ERROR_MESSAGE)
                .textContent()
                .trim();

        assertThat(actualErrorMessage)
                .as("Error message for empty Last Name")
                .isEqualTo(expectedErrorMessage);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneWithEmptyPostalCode() {
        int FIRST_ITEM = 0;

        String firstName = "John";
        String lastName = "Doe";
        String expectedErrorMessage = "Error: Postal Code is required";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .clickContinueButton();

        String actualErrorMessage = page
                .locator(CheckoutStepOnePageLocators.ERROR_MESSAGE)
                .textContent()
                .trim();

        assertThat(actualErrorMessage)
                .as("Error message for empty Postal Code")
                .isEqualTo(expectedErrorMessage);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCancelButtonNavigatesToCartPage() {
        int FIRST_ITEM = 0;

        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepOnePage checkoutStepOnePage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout();

        assertThat(page.url())
                .as("Checkout Step One page URL before clicking Cancel")
                .isEqualTo(AppConstants.CHECKOUT_STEP_ONE_URL);

        checkoutStepOnePage.clickCancelButton();

        assertThat(page.url())
                .as("Cart page URL after clicking Cancel")
                .isEqualTo(AppConstants.CART_URL);

        appStateUtils.resetStateAndLogout();
    }
}
