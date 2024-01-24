package com.luma.testUtils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
public class Page {
    @FindBy(css = "img[alt='Loading...']")
    private WebElement loadingCircle;
    protected WebDriverWait webDriverWait;
    protected WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds(5));
    }

    protected void clickOn(WebElement webElement) {
        try {
            waitForElementToBeClickable(webElement).click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", waitForElementToBeClickable(webElement));
        }
    }

    protected void enterInputInField(WebElement webElement, String input) {
        waitForVisibility(webElement).sendKeys(input);
    }

    protected void clearField(WebElement webElement) {
        waitForVisibility(webElement).clear();
    }

    protected String getTextFromElement(WebElement webElement) {
        String text = waitForVisibility(webElement).getText();
        if(text.isEmpty()) {
            text = webElement.getAttribute("value");
        }
        return text;
    }

    protected String getAttributeValueFromElement(WebElement webElement, String attribute) {
        return waitForVisibility(webElement).getAttribute(attribute);
    }

    protected WebElement waitForVisibility(WebElement webElement) {
        return webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
    }
    protected boolean waitForTextOfTheElementToBe(WebElement webElement, String text) {
        return webDriverWait.until(ExpectedConditions.textToBePresentInElement(webElement, text));
    }
    protected WebElement waitForElementToBeClickable(WebElement webElement) {
        return webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    protected List<WebElement> waitForVisibilityOfAllElements(List<WebElement> webElements) {
        return webDriverWait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    protected void waitForInvisibilityOfAllElements(List<WebElement> webElements) {
        webDriverWait.until(ExpectedConditions.invisibilityOfAllElements(webElements));
    }

    protected void waitForLoadingToFinish() {
        waitForInvisibilityOfAllElements(driver.findElements(By.cssSelector("img[alt='Loading...']")));
    }
}
