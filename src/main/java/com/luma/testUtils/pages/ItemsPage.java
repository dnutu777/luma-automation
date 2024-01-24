package com.luma.testUtils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
public class ItemsPage extends Page {
    @FindBy(className = "details")
    private List<WebElement> itemDetailsList;

    public ItemsPage(WebDriver driver) {
        super(driver);
    }

    public void selectSizeAndColorForAnItem(int itemIndex, int sizeIndex, int colorIndex) {
        waitForVisibilityOfAllElements(itemDetailsList);
        clickOn(itemDetailsList.get(itemIndex).findElement(By.xpath("div[2]/div/div/div[" + sizeIndex + "]")));
        clickOn(itemDetailsList.get(itemIndex).findElement(By.xpath("div[2]/div[2]/div/div[" + colorIndex + "]")));
    }

    public String getItemTitle(int itemIndex) {
        return getTextFromElement(itemDetailsList.get(itemIndex).findElement(By.xpath("strong")));
    }

    public String getItemPrice(int itemIndex) {
        return getTextFromElement(itemDetailsList.get(itemIndex).findElement(By.xpath("div/span/span/span[2]/span")));
    }

    public String getItemSize(int itemIndex, int sizeIndex) {
        return getTextFromElement(itemDetailsList.get(itemIndex).findElement(By.xpath("div[2]/div/div/div[@index='" + sizeIndex + "']")));
    }

    public String getItemColor(int itemIndex, int colorIndex) {
        return getAttributeValueFromElement(itemDetailsList.get(itemIndex).findElement(By.xpath("div[2]/div[2]/div/div[" + colorIndex + "]")), "option-tooltip-value");
    }

    public void clickOnAddToCartButton(int itemIndex) {
        clickOn(waitForElementToBeClickable(itemDetailsList.get(itemIndex).findElement(By.xpath("div[3]//button"))));
        waitForLoadingToFinish();
    }

    public WebElement getItemDetails(int itemIndex) {
        return waitForVisibilityOfAllElements(itemDetailsList).get(itemIndex);
    }
}
