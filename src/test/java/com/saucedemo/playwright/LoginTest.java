package com.saucedemo.playwright;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    @Test
    public void testLoginPageLoad() {
        SoftAssertions softly = new SoftAssertions();
        LoginPage loginPage = new LoginPage(page).navigate();

        assertThat(loginPage.getPage().url())
                .as("Login page URL")
                .contains(AppConstants.BASE_URL);

        softly.assertThat(loginPage.isUsernameInputVisible())
                .as("Username input visible")
                .isTrue();

        softly.assertThat(loginPage.isPasswordInputVisible())
                .as("Password input visible")
                .isTrue();

        softly.assertThat(loginPage.isLoginButtonVisible())
                .as("Login button visible")
                .isTrue();

        softly.assertAll();
    }

    @Test
    public void testValidLogin() {
        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .enterUsername("standard_user")
                .enterPassword("secret_sauce")
                .clickLoginButton();

        assertThat(inventoryPage.getPage().url())
                .as("Inventory page URL")
                .isEqualTo(AppConstants.INVENTORY_URL);

        softly.assertThat(inventoryPage.isBurgerMenuVisible())
                .as("Burger menu visible")
                .isTrue();

        softly.assertThat(inventoryPage.isShoppingCartVisible())
                .as("Shopping cart visible")
                .isTrue();

        softly.assertThat(inventoryPage.isSortDropdownVisible())
                .as("Sort dropdown visible")
                .isTrue();

        softly.assertThat(inventoryPage.areItemsVisible())
                .as("Inventory items visible")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInvalidLogin() {
        SoftAssertions softly = new SoftAssertions();
        LoginPage loginPage = new LoginPage(page).navigate();

        loginPage.enterUsername("invalid_username")
                .enterPassword("invalid_password")
                .clickLoginButton();

        softly.assertThat(loginPage.isErrorMessageDisplayed())
                .as("Error message displayed")
                .isTrue();

        softly.assertThat(loginPage.hasErrorMessage("Username and password do not match"))
                .as("Error message contains correct text")
                .isTrue();

        loginPage.clearAllFields();
        softly.assertAll();
    }
}