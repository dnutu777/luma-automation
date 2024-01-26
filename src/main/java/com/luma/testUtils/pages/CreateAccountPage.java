package com.luma.testUtils.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateAccountPage extends Page {
    @FindBy(css = "span[data-ui-id='page-title-wrapper']")
    private WebElement title;

    public CreateAccountPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkTitleIsDisplayed() {
        return waitForVisibility(title).isDisplayed();
    }
}
