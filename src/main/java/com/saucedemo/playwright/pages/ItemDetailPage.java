package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.constants.ItemDetailPageLocators;

public class ItemDetailPage extends BasePage {

    private final Locator itemName;
    private final Locator itemDescription;
    private final Locator itemPrice;
    private final Locator itemImage;
    private final Locator addToCartButton;
    private final Locator removeButton;
    private final Locator backToProductsButton;

    public ItemDetailPage(Page page) {
        super(page);
        itemName                = locator(ItemDetailPageLocators.ITEM_NAME);
        itemDescription         = locator(ItemDetailPageLocators.ITEM_DESCRIPTION);
        itemPrice               = locator(ItemDetailPageLocators.ITEM_PRICE);
        itemImage               = locator(ItemDetailPageLocators.ITEM_IMAGE);
        addToCartButton         = locator(ItemDetailPageLocators.ADD_TO_CART_BUTTON);
        removeButton            = locator(ItemDetailPageLocators.REMOVE_BUTTON);
        backToProductsButton    = locator(ItemDetailPageLocators.BACK_TO_PRODUCTS_BUTTON);
    }

    // ------------------ Visibility Methods ------------------
    public boolean isRedirectedToItemDetailPage() {
        return page.url().contains(AppConstants.ITEM_DETAIL_URL);
    }

    public boolean isItemNameVisibleAndNotBlank() {
        return itemName.isVisible() && !itemName.innerText().trim().isEmpty();
    }

    public boolean isItemDescriptionVisibleAndNotBlank() {
        return itemDescription.isVisible() && !itemDescription.innerText().trim().isEmpty();
    }

    public boolean isItemPriceVisibleAndNotBlank() {
        return itemPrice.isVisible() && !itemPrice.innerText().trim().isEmpty();
    }

    public boolean isItemImageVisible() {
        return itemImage.isVisible();
    }

    public boolean isBackToProductsButtonVisible() {
        return backToProductsButton.isVisible();
    }

    // ------------------ Action Methods ------------------
    public InventoryPage clickBackToProducts() {
        backToProductsButton.click();
        return new InventoryPage(page);
    }

    public ItemDetailPage clickAddToCartButton() {
        addToCartButton.click();
        return this;
    }

    public ItemDetailPage clickRemoveButton() {
        removeButton.click();
        return this;
    }

    // ------------------ Getter Methods ------------------
    public String getItemName() {
        return itemName.innerText().trim();
    }

    public String getItemDescription() {
        return itemDescription.innerText().trim();
    }

    public Double getItemPrice() {
        String priceText = itemPrice.innerText().trim().replace("$", "");
        return Double.parseDouble(priceText);
    }
}