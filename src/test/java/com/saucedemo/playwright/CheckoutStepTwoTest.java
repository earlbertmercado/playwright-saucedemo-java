package com.saucedemo.playwright;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.pages.CheckoutStepTwoPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepTwoTest extends BaseTest {

    @Test
    public void testCheckoutStepTwoPageLoad() {
        int FIRST_ITEM = 0;

        // Externalize test data later
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

        assertThat(page.url())
                .as("Checkout Step Two page URL")
                .isEqualTo(AppConstants.CHECKOUT_STEP_TWO_URL);

        softly.assertThat(checkoutStepTwoPage.getPageTitle())
                .as("Checkout Step Two page title")
                .isEqualTo("Checkout: Overview");

        softly.assertThat(checkoutStepTwoPage.isFinishButtonVisible())
                .as("Finish button visible")
                .isTrue();

        softly.assertThat(checkoutStepTwoPage.isCancelButtonVisible())
                .as("Cancel button visible")
                .isTrue();

        softly.assertThat(checkoutStepTwoPage.areItemDetailsValid(FIRST_ITEM))
                .as("Item details are valid")
                .isTrue();

        softly.assertThat(checkoutStepTwoPage.getTotalBeforeTax())
                .as("Total before tax is not null")
                .isNotNull();

        softly.assertThat(checkoutStepTwoPage.getTax())
                .as("Tax is not null")
                .isNotNull();

        softly.assertThat(checkoutStepTwoPage.getTotalAfterTax())
                .as("Total after tax is not null")
                .isNotNull();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepMultipleItemTotals() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;

        // Externalize test data later
        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepTwoPage checkoutStepTwoPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .addItemToCartByIndex(SECOND_ITEM)
                .addItemToCartByIndex(THIRD_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode)
                .clickContinueButton();

        Double expectedTotalBeforeTax = checkoutStepTwoPage.getTotalItemPrices();
        Double actualTotalBeforeTax = checkoutStepTwoPage.getTotalBeforeTax();

        softly.assertThat(actualTotalBeforeTax)
                .as("Total before tax matches sum of item prices")
                .isEqualTo(expectedTotalBeforeTax);

        Double tax = checkoutStepTwoPage.getTax();
        Double expectedTotalAfterTax = expectedTotalBeforeTax + tax;
        Double actualTotalAfterTax = checkoutStepTwoPage.getTotalAfterTax();

        softly.assertThat(actualTotalAfterTax)
                .as("Total after tax matches total before tax plus tax")
                .isEqualTo(expectedTotalAfterTax);

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepTwoCancelButton() {
        int FIRST_ITEM = 0;

        // Externalize test data later
        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";

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

        checkoutStepTwoPage.clickCancelButton();

        assertThat(page.url())
                .as("URL after clicking Cancel button")
                .isEqualTo(AppConstants.INVENTORY_URL);

        appStateUtils.resetStateAndLogout();
    }
}
