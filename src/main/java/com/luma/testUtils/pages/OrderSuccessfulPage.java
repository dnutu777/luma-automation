package com.luma.testUtils.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OrderSuccessfulPage extends Page {
    @FindBy(css = "div[class='checkout-success'] > p > span")
    private WebElement orderId;
    @FindBy(linkText = "Continue Shopping")
    private WebElement continueShoppingButton;
    @FindBy(linkText = "Create an Account")
    private WebElement createAnAccountButton;
    @FindBy(css = "span[data-bind='text: getEmailAddress()']")
    private WebElement email;

    public OrderSuccessfulPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkOrderIdIsDisplayed() {
        return waitForVisibility(orderId).isDisplayed();
    }

    public String getOrderId() {
        return getTextFromElement(orderId);
    }

    public boolean checkThatTheCorrectEmailIsDisplayed(String emailValue) {
        return getTextFromElement(email).equals(emailValue);
    }

    public void clickOnButton(String button) {
        switch (button) {
            case "Continue Shopping":
                clickOn(continueShoppingButton);
                break;
            default:
                clickOn(createAnAccountButton);
        }
    }
}
