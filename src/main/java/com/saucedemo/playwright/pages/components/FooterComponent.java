package com.saucedemo.playwright.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.FooterLocators;

/**
 * Represents the footer section of the page.
 * Provides access to social media links and footer copyright text.
 */
public class FooterComponent extends BasePage {

    private final Locator twitterLink;
    private final Locator facebookLink;
    private final Locator linkedinLink;
    private final Locator footerCopyright;

    public FooterComponent(Page page) {
        super(page);
        twitterLink     = locator(FooterLocators.TWITTER_LINK);
        facebookLink    = locator(FooterLocators.FACEBOOK_LINK);
        linkedinLink    = locator(FooterLocators.LINKEDIN_LINK);
        footerCopyright = locator(FooterLocators.FOOTER_COPYRIGHT);
    }

    // ------------------ Visibility Methods ------------------
    public boolean isTwitterLinkVisible() {
        return twitterLink.isVisible();
    }

    public boolean isFacebookLinkVisible() {
        return facebookLink.isVisible();
    }

    public boolean isLinkedinLinkVisible() {
        return linkedinLink.isVisible();
    }

    // ------------------ Getter Methods ------------------
    public String getFooterCopyrightText() {
        return footerCopyright.innerText();
    }

    // ------------------ Action Methods ------------------
    public void clickTwitterLink() {
        twitterLink.click();
    }

    public void clickFacebookLink() {
        facebookLink.click();
    }

    public void clickLinkedinLink() {
        linkedinLink.click();
    }
}
