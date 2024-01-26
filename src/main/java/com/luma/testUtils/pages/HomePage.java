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

    public void clickOnButton(String button) {
        switch (button) {
            case "What's New":
                clickOn(whatsNewButton);
                break;
            default:
                clickOn(cartButton);
        }
    }

    public void waitForTheCartCounterToBeUpdated(String counterValue) {
        waitForVisibility(fluentWait.until((driver -> cartItemCounter)));
        waitForTextOfTheElementToBe(cartItemCounter, counterValue);
    }

    public boolean checkWhatsNewButtonIsDisplayed() {
        return waitForVisibility(whatsNewButton).isDisplayed();
    }
}
