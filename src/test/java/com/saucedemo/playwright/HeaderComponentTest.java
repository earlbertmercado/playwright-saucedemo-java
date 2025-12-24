package com.saucedemo.playwright;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.pages.CartPage;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.ItemDetailPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.components.HeaderComponent;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HeaderComponentTest extends BaseTest {

    @Test
    public void testBurgerMenuAllItemsLink() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .clickItemNameByIndex(FIRST_ITEM);

        header.clickAllItemsLink();

        assertThat(page.url())
                .as("Inventory page URL after clicking All Items link")
                .isEqualTo(AppConstants.INVENTORY_URL);

        softly.assertThat(new InventoryPage(page).getPageTitle())
                .as("Inventory page title after clicking All Items link")
                .isEqualTo("Products");

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testBurgerMenuAboutLink() {
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce");

        header.clickAboutLink();

        assertThat(page.url())
                .as("Sauce Labs About page URL after clicking About link")
                .isEqualTo(AppConstants.SAUCE_LABS_URL);

        page.goBack();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testBurgerMenuLogoutLink() {
        HeaderComponent header = new HeaderComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce");

        LoginPage loginPage = header.clickLogoutLink();

        assertThat(page.url())
                .as("Login page URL after clicking Logout link")
                .isEqualTo(AppConstants.BASE_URL + "/");

        assertThat(loginPage.isLoginButtonVisible())
                .as("Login button visible after logout")
                .isTrue();
    }

    @Test
    public void testResetAppStateLink() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM);

        softly.assertThat(header.isShoppingCartBadgeVisible())
                .as("Shopping cart badge visible before resetting app state")
                .isTrue();

        header.clickResetAppStateLink();

        softly.assertThat(header.isShoppingCartBadgeVisible())
                .as("Shopping cart badge not visible after resetting app state")
                .isFalse();

        softly.assertThat(inventoryPage.getAddToCartButtons()
                        .allTextContents()
                        .stream()
                        .allMatch(text -> text.equals("Add to cart")))
                .as("All buttons show 'Add to cart' after resetting app state")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCartBadgePersistence() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;
        int EXPECTED_CART_BADGE_COUNT = 3;

        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login("standard_user", "secret_sauce")
                .addItemToCartByIndex(FIRST_ITEM)
                .addItemToCartByIndex(SECOND_ITEM)
                .addItemToCartByIndex(THIRD_ITEM)
                .clickShoppingCart();

        Assertions.assertThat(cartPage.getCartItemCount())
                .as("Number of items in cart")
                .isEqualTo(EXPECTED_CART_BADGE_COUNT);

        Assertions.assertThat(header.getShoppingCartBadgeText())
                .as("Shopping cart badge shows correct item count")
                .isEqualTo(String.valueOf(EXPECTED_CART_BADGE_COUNT));

        page.reload();

        Assertions.assertThat(header.getShoppingCartBadgeText())
                .as("Shopping cart badge persists after page reload")
                .isEqualTo(String.valueOf(EXPECTED_CART_BADGE_COUNT));

        cartPage.clickContinueShopping();

        Assertions.assertThat(header.getShoppingCartBadgeText())
                .as("Shopping cart badge persists after continuing shopping")
                .isEqualTo(String.valueOf(EXPECTED_CART_BADGE_COUNT));

        appStateUtils.resetStateAndLogout();
    }
}