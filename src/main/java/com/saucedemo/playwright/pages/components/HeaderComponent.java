package com.saucedemo.playwright.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.HeaderLocators;
import com.saucedemo.playwright.pages.LoginPage;

public class HeaderComponent extends BasePage {

    private final Locator shoppingCart;
    private final Locator shoppingCartBadge;
    private final Locator burgerMenuButton;
    private final Locator allItemsLink;
    private final Locator aboutLink;
    private final Locator logoutLink;
    private final Locator resetAppStateLink;

    public HeaderComponent(Page page) {
        super(page);
        shoppingCart        = locator(HeaderLocators.SHOPPING_CART);
        shoppingCartBadge   = locator(HeaderLocators.SHOPPING_CART_BADGE);
        burgerMenuButton    = locator(HeaderLocators.BURGER_MENU_BUTTON);
        allItemsLink        = locator(HeaderLocators.ALL_ITEMS_LINK);
        aboutLink           = locator(HeaderLocators.ABOUT_LINK);
        logoutLink          = locator(HeaderLocators.LOGOUT_LINK);
        resetAppStateLink   = locator(HeaderLocators.RESET_APP_STATE_LINK);
    }

    // ------------------ Visibility Methods ------------------
    public boolean isShoppingCartVisible() {
        return shoppingCart.isVisible();
    }

    public boolean isShoppingCartBadgeVisible() {
        return shoppingCartBadge.isVisible();
    }

    // ------------------ Getter Methods ------------------
    public String getShoppingCartBadgeText() {
        return shoppingCartBadge.innerText();
    }

    // ------------------ Action Methods ------------------
    public void clickBurgerMenu() {
        burgerMenuButton.click();
    }

    public void clickAllItemsLink() {
        clickBurgerMenu();
        allItemsLink.click();
    }

    public void clickAboutLink() {
        clickBurgerMenu();
        aboutLink.click();
    }

    public LoginPage clickLogoutLink() {
        clickBurgerMenu();
        logoutLink.click();
        return new LoginPage(page);
    }

    public void clickResetAppStateLink() {
        clickBurgerMenu();
        resetAppStateLink.click();
        page.reload();
    }
}
