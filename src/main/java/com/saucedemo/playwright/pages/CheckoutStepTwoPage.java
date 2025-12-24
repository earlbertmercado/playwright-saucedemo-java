package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.CheckoutStepTwoPageLocators;
import com.saucedemo.playwright.constants.HeaderLocators;

public class CheckoutStepTwoPage extends BasePage {

    private final Locator pageTitle;
    private final Locator finishButton;
    private final Locator cancelButton;
    private final Locator cartItems;
    private final Locator itemNames;
    private final Locator itemPrices;
    private final Locator itemDescriptions;
    private final Locator itemQuantities;
    private final Locator itemTotal;
    private final Locator tax;
    private final Locator total;

    public CheckoutStepTwoPage(Page page) {
        super(page);
        pageTitle        = locator(HeaderLocators.PAGE_TITLE);
        finishButton     = locator(CheckoutStepTwoPageLocators.FINISH_BUTTON);
        cancelButton     = locator(CheckoutStepTwoPageLocators.CANCEL_BUTTON);
        cartItems        = locator(CheckoutStepTwoPageLocators.CART_ITEMS);
        itemNames        = locator(CheckoutStepTwoPageLocators.ITEM_NAMES);
        itemPrices       = locator(CheckoutStepTwoPageLocators.ITEM_PRICES);
        itemDescriptions = locator(CheckoutStepTwoPageLocators.ITEM_DESCRIPTIONS);
        itemQuantities   = locator(CheckoutStepTwoPageLocators.ITEM_QUANTITIES);
        itemTotal        = locator(CheckoutStepTwoPageLocators.ITEM_TOTAL);
        tax              = locator(CheckoutStepTwoPageLocators.TAX);
        total            = locator(CheckoutStepTwoPageLocators.TOTAL);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    public int getNumberOfCartItems() {
        return cartItems.count();
    }

    public String getItemName(int index) {
        return itemNames.nth(index).innerText();
    }

    public Double getItemPrice(int index) {
        String priceText = itemPrices.nth(index).innerText().replace("$", "");
        return Double.parseDouble(priceText);
    }

    public String getItemDescription(int index) {
        return itemDescriptions.nth(index).innerText();
    }

    public String getItemQuantity(int index) {
        return itemQuantities.nth(index).innerText();
    }

    public Double getTotalItemPrices() {
        double total = 0.0;
        int count = getNumberOfCartItems();
        for (int i = 0; i < count; i++) {
            total += getItemPrice(i);
        }
        return total;
    }

    public Double getTotalBeforeTax() {
        String amountText = itemTotal.innerText().replace("Item total: $", "");
        return Double.parseDouble(amountText);
    }

    public Double getTax() {
        String amountText = tax.innerText().replace("Tax: $", "");
        return Double.parseDouble(amountText);
    }

    public Double getTotalAfterTax() {
        String amountText = total.innerText().replace("Total: $", "");
        return Double.parseDouble(amountText);
    }

    // ------------------ Visibility Methods ------------------
    public boolean isFinishButtonVisible() {
        return finishButton.isVisible();
    }

    public boolean isCancelButtonVisible() {
        return cancelButton.isVisible();
    }

    // ------------------ Validation Methods ------------------
    public boolean areItemDetailsValid(int index) {
        return !getItemName(index).isEmpty() &&
                !getItemDescription(index).isEmpty() &&
                getItemPrice(index) != null &&
                !getItemQuantity(index).isEmpty();
    }

    // ------------------ Action Methods ------------------
    public CheckoutCompletePage clickFinishButton() {
        finishButton.click();
        return new CheckoutCompletePage(page);
    }

    public InventoryPage clickCancelButton() {
        cancelButton.click();
        return new InventoryPage(page);
    }
}
