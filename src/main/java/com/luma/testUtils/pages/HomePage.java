package com.luma.testUtils.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
public class HomePage extends Page {
    @FindBy(linkText = "What's New")
    private WebElement whatsNewButton;
    @FindBy(className = "showcart")
    private WebElement cartButton;
    @FindBy(className = "counter-number")
    private WebElement cartItemCounter;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickOnWhatsNew() {
        clickOn(whatsNewButton);
    }

    public void clickOnCartButton() {
        clickOn(cartButton);
    }

    public void waitForTheCartCounterToBeUpdated() {
        waitForVisibility(cartItemCounter);
        waitForTextOfTheElementToBe(cartItemCounter, "1");
    }

}
