package com.luma.testUtils.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends Page {
    @FindBy(id = "top-cart-btn-checkout")
    private WebElement proceedToCheckoutButton;
    @FindBy(className = "item-qty")
    private List<WebElement> quantityFieldsList;
    @FindBy(css = ".amount > span > span")
    private WebElement cartSubtotal;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnProceedToCheckoutButton() {
        clickOn(proceedToCheckoutButton);
        waitForLoadingToFinish();
    }

    public String getItemQuantity(int itemIndex) {
        waitForVisibilityOfAllElements(quantityFieldsList);
        return getTextFromElement(quantityFieldsList.get(itemIndex));
    }

    public String getCartSubtotal() {
        return getTextFromElement(cartSubtotal);
    }
}
