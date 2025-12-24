package com.saucedemo.playwright;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.constants.HeaderLocators;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.ItemDetailPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryTest extends BaseTest {

    @Test
    public void testInventoryPageLoad() {
        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce");

        assertThat(inventoryPage.getPage().url())
                .as("Inventory page URL")
                .isEqualTo(AppConstants.INVENTORY_URL);

        softly.assertThat(inventoryPage.getPageTitle())
                .as("Inventory page title")
                .isEqualTo("Products");

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

        softly.assertThat(inventoryPage.areAddToCartButtonsVisible())
                .as("Add to cart buttons visible for each item")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByNameAsc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .sortByNameAsc();

        assertThat(inventoryPage.isSortedAlphabeticallyAsc())
                .as("Inventory sorted by name ascending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByNameDesc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .sortByNameDesc();

        assertThat(inventoryPage.isSortedAlphabeticallyDesc())
                .as("Inventory sorted by name descending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByPriceAsc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .sortByPriceAsc();

        assertThat(inventoryPage.isSortedByPriceAsc())
                .as("Inventory sorted by price ascending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByPriceDesc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .sortByPriceDesc();

        assertThat(inventoryPage.isSortedByPriceDesc())
                .as("Inventory sorted by price descending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testEachItemDetails() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce");

        assertThat(inventoryPage.areAllItemsValid())
                .as("All inventory items have valid details")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testShoppingCartBadgeAfterAddingItems() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;
        int EXPECTED_CART_BADGE_COUNT = 3;

        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .addItemToCartByIndex(SECOND_ITEM)
                .addItemToCartByIndex(THIRD_ITEM);

        assertThat(inventoryPage.getCartItemCount())
                .as("Shopping cart badge item count after adding items")
                .isEqualTo(EXPECTED_CART_BADGE_COUNT);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testShoppingCartBadgeAfterRemovingItems() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;

        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .addItemToCartByIndex(SECOND_ITEM)
                .addItemToCartByIndex(THIRD_ITEM)
                .removeItemFromCartByIndex(FIRST_ITEM)
                .removeItemFromCartByIndex(SECOND_ITEM)
                .removeItemFromCartByIndex(THIRD_ITEM);

        assertThat(inventoryPage
                .getPage()
                .locator(HeaderLocators.SHOPPING_CART_BADGE)
                .isVisible())
                .as("Shopping cart badge is invisible after removing all items")
                .isFalse();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testClickingItemNavigatesToItemDetailPage() {
        int FIRST_ITEM = 0;

        AppStateUtils appStateUtils = new AppStateUtils(page);
        InventoryPage inventoryPage = new InventoryPage(page);

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .clickItemNameByIndex(FIRST_ITEM);

        assertThat(itemDetailPage.isRedirectedToItemDetailPage())
                .as("Redirected to item detail page when clicking item name")
                .isTrue();

        itemDetailPage.clickBackToProducts();
        inventoryPage.clickItemImageByIndex(FIRST_ITEM);

        assertThat(itemDetailPage.isRedirectedToItemDetailPage())
                .as("Redirected to item detail page when clicking item image")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }
}