package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.constants.LoginPageLocators;

public class LoginPage extends BasePage {

    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        super(page);
        usernameInput   = locator(LoginPageLocators.USERNAME_INPUT);
        passwordInput   = locator(LoginPageLocators.PASSWORD_INPUT);
        loginButton     = locator(LoginPageLocators.LOGIN_BUTTON);
        errorMessage    = locator(LoginPageLocators.ERROR_MESSAGE);
    }

    public LoginPage navigate() {
        page.navigate(AppConstants.BASE_URL);
        return this;
    }

    // ------------------ Action Methods ------------------
    public LoginPage enterUsername(String username) {
        usernameInput.fill(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordInput.fill(password);
        return this;
    }

    public InventoryPage clickLoginButton() {
        loginButton.click();
        return new InventoryPage(page);
    }

    public InventoryPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }

    public void clearUsername() {
        usernameInput.clear();
    }

    public void clearPassword() {
        passwordInput.clear();
    }

    public void clearAllFields() {
        clearUsername();
        clearPassword();
    }

    // ------------------ Verification Methods ------------------
    public boolean isUsernameInputVisible() {
        return usernameInput.isVisible();
    }

    public boolean isPasswordInputVisible() {
        return passwordInput.isVisible();
    }

    public boolean isLoginButtonVisible() {
        return loginButton.isVisible();
    }

    public boolean isErrorMessageDisplayed() {
        return errorMessage.isVisible();
    }

    public String getErrorMessage() {
        return errorMessage.textContent().trim();
    }

    public boolean hasErrorMessage(String expectedError) {
        return isErrorMessageDisplayed()
                && getErrorMessage().contains(expectedError);
    }
}
