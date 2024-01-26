package com.luma.testUtils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Page {
    @FindBy(css = "img[alt='Loading...']")
    private List<WebElement> loadingCircles;
    protected WebDriverWait webDriverWait;
    protected Wait<WebDriver> fluentWait;
    protected WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(1))
                .ignoring(Exception.class);
    }

    protected void clickOn(WebElement webElement) {
        waitForVisibility(webElement);
        waitForElementToBeClickable(webElement).click();
    }

    protected void enterInputInField(WebElement webElement, String input) {
        waitForVisibility(webElement).sendKeys(input);
    }

    protected void clearField(WebElement webElement) {
        waitForVisibility(webElement).clear();
    }

    protected String getTextFromElement(WebElement webElement) {
        String text = waitForVisibility(webElement).getText();
        if (text.isEmpty()) {
            text = waitForVisibility(webElement).getAttribute("value");
        }
        return text;
    }

    protected String getAttributeValueFromElement(WebElement webElement, String attribute) {
        return waitForVisibility(webElement).getAttribute(attribute);
    }

    protected WebElement waitForVisibility(WebElement webElement) {
        return webDriverWait.until(ExpectedConditions.visibilityOf(fluentWait.until((driver -> webElement))));
    }

    protected boolean waitForTextOfTheElementToBe(WebElement webElement, String text) {
        return webDriverWait.until(ExpectedConditions.textToBePresentInElement(fluentWait.until((driver -> webElement)), text));
    }

    protected WebElement waitForElementToBeClickable(WebElement webElement) {
        return webDriverWait.until(ExpectedConditions.elementToBeClickable((WebElement) fluentWait.until(driver -> webElement)));
    }

    protected List<WebElement> waitForVisibilityOfAllElements(List<WebElement> webElements) {
        for (WebElement element : webElements) {
            fluentWait.until((driver -> element));
        }
        return webDriverWait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    protected void waitForInvisibilityOfAllElements(List<WebElement> webElements) {
        for (WebElement element : webElements) {
            fluentWait.until((driver -> element));
        }

        webDriverWait.until(ExpectedConditions.invisibilityOfAllElements(webElements));
    }

    protected void waitForLoadingToFinish() {
        for (WebElement element : loadingCircles) {
            fluentWait.until((driver -> element));
        }

        waitForInvisibilityOfAllElements(loadingCircles);
    }
}
