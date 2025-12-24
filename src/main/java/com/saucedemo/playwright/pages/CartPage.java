package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.CartPageLocators;
import com.saucedemo.playwright.constants.HeaderLocators;

public class CartPage extends BasePage {

    private final Locator pageTitle;
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;
    private final Locator cartItems;
    private final Locator itemNames;
    private final Locator itemPrices;
    private final Locator itemDescriptions;
    private final Locator itemQuantities;
    private final Locator removeButtons;

    public CartPage(Page page) {
        super(page);
        pageTitle              = locator(HeaderLocators.PAGE_TITLE);
        checkoutButton         = locator(CartPageLocators.CHECKOUT_BUTTON);
        continueShoppingButton = locator(CartPageLocators.CONTINUE_SHOPPING_BUTTON);
        cartItems              = locator(CartPageLocators.CART_ITEMS);
        itemNames              = locator(CartPageLocators.ITEM_NAMES);
        itemPrices             = locator(CartPageLocators.ITEM_PRICES);
        itemDescriptions       = locator(CartPageLocators.ITEM_DESCRIPTIONS);
        itemQuantities         = locator(CartPageLocators.ITEM_QUANTITIES);
        removeButtons          = locator(CartPageLocators.REMOVE_BUTTONS);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    public int getCartItemCount() {
        return cartItems.count();
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (int i = 0; i < itemQuantities.count(); i++) {
            String quantityText = itemQuantities.nth(i).innerText();
            totalQuantity += Integer.parseInt(quantityText);
        }
        return totalQuantity;
    }

    // ------------------ Visibility Methods ------------------
    public boolean isCheckoutButtonVisible() {
        return checkoutButton.isVisible();
    }

    public boolean isContinueShoppingButtonVisible() {
        return continueShoppingButton.isVisible();
    }

    // ------------------ Validation Methods ------------------
    public boolean areAllItemDetailsValid() {
        for (int i = 0; i < cartItems.count(); i++) {
            if (!isValidItem(i)) return false;
        }
        return true;
    }

    public boolean isValidItem(int index) {
        String name = itemNames.nth(index).innerText();
        String description = itemDescriptions.nth(index).innerText();
        String priceText = itemPrices.nth(index).innerText().replace("$", "");

        if (name.isEmpty() || description.isEmpty()) return false;

        // Returns true if priceText is a valid numeric value (integer or decimal)
        return priceText.matches("\\d+(\\.\\d+)?");
    }

    // ------------------ Action Methods ------------------
    public void removeItemByIndex(int index) {
        removeButtons.nth(index).click();
    }

    public InventoryPage clickContinueShopping() {
        continueShoppingButton.click();
        return new InventoryPage(page);
    }

    public CheckoutStepOnePage clickCheckout() {
        checkoutButton.click();
        return new CheckoutStepOnePage(page);
    }
}
