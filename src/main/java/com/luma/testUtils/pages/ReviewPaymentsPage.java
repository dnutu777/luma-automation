package com.luma.testUtils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
public class ReviewPaymentsPage extends Page {
    @FindBy(id = "block-discount-heading")
    private WebElement applyDiscountCodeButton;
    @FindBy(id = "discount-code")
    private WebElement discountCodeField;
    @FindBy(className = "action-apply")
    private WebElement applyDiscountButton;
    @FindBy(className = "message-error")
    private WebElement discountCodeErrorMessage;
    @FindBy(css = "span[data-th='Shipping']")
    private WebElement shippingPrice;
    @FindBy(css = "span[data-bind='text: getShippingMethodTitle()']")
    private WebElement shippingMethodAndCarrier;
    @FindBy(css = "span[data-th='Cart Subtotal']")
    private WebElement cartSubtotal;
    @FindBy(xpath = "(//span[@class='price'])[7]")
    private WebElement orderTotal;

    public ReviewPaymentsPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkThatTheDiscountCodeFieldIsDisplayed() {
        return discountCodeField.isDisplayed();
    }

    public boolean checkThatTheApplyDiscountButtonIsDisplayed() {
        return applyDiscountButton.isDisplayed();
    }

    public void clickOnApplyDiscountCodeButton() {
        clickOn(waitForElementToBeClickable(applyDiscountCodeButton));
    }

    public void clickOnApplyDiscountButton() {
        clickOn(waitForElementToBeClickable(applyDiscountButton));
        waitForLoadingToFinish();
    }

    public boolean checkDiscountCodeFieldValidationMessage(String message) {
        return getTextFromElement(discountCodeField.findElement(By.xpath("following-sibling::div"))).equals(message);
    }

    public boolean checkDiscountCodeFieldErrorMessage(String message) {
        return getTextFromElement(discountCodeErrorMessage).equals(message);
    }

    public void enterDiscountCode(String input) {
        enterInputInField(discountCodeField, input);
    }

    public boolean checkShippingDetails(String details) {
        return details.equals(getTextFromElement(shippingPrice)+ " " + getTextFromElement(shippingMethodAndCarrier));
    }

    public boolean checkCartSubtotal(String subtotal) {
        return getTextFromElement(cartSubtotal).equals(subtotal);
    }

    public boolean checkOrderTotal(String total) {
        return getTextFromElement(orderTotal).equals(total + "0");
    }
}
