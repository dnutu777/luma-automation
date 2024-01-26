package com.luma.testUtils.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WhatsNewPage extends Page {
    @FindBy(linkText = "Jackets")
    private WebElement jacketsButton;

    public WhatsNewPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnJackets() {
        clickOn(jacketsButton);
    }
}
