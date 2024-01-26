package com.luma.testUtils.pages;

import com.luma.testUtils.testObjects.ShippingDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ShippingPage extends Page {
    @FindBy(xpath = "(//*[@id='customer-email'])[1]")
    private WebElement emailAddressField;
    @FindBy(name = "firstname")
    private WebElement firstNameField;
    @FindBy(name = "lastname")
    private WebElement lastNameField;
    @FindBy(name = "company")
    private WebElement companyField;
    @FindBy(name = "street[0]")
    private WebElement firstStreetAddressField;
    @FindBy(name = "street[1]")
    private WebElement secondStreetAddressField;
    @FindBy(name = "street[2]")
    private WebElement thirdStreetAddressField;
    @FindBy(name = "city")
    private WebElement cityField;
    @FindBy(name = "region_id")
    private WebElement stateProvinceDropdown;
    @FindBy(name = "postcode")
    private WebElement zipPostalCodeField;
    @FindBy(name = "country_id")
    private WebElement countryDropdown;
    @FindBy(name = "telephone")
    private WebElement phoneNumberField;
    @FindBy(xpath = "//span[@id='tooltip']")
    private List<WebElement> tooltipsList;
    @FindBy(xpath = "//div[@class='field-tooltip-content']")
    private List<WebElement> tooltipsContentList;
    @FindBy(css = "tr[data-bind='click: element.selectShippingMethod']")
    private List<WebElement> shippingMethodsList;
    @FindBy(className = "continue")
    private WebElement nextButton;
    @FindBy(className = "items-in-cart")
    private WebElement itemInCartButton;
    @FindBy(css = "span[data-bind='text: getCartSummaryItemsCount()']")
    private WebElement itemCounter;
    @FindBy(className = "product-item-inner")
    private List<WebElement> itemInformationList;
    @FindBy(className = "options")
    private List<WebElement> viewDetailsButtonsList;
    @FindBy(xpath = "//*[@id='co-shipping-method-form']/div[3]")
    private WebElement shippingMethodErrorMessage;

    public ShippingPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkErrorMessage(String field, String message) {
        switch (field) {
            case "Shipping Method":
                return getTextFromElement(waitForVisibility(shippingMethodErrorMessage)).equals(message);
            case "Email":
                return getTextFromElement(waitForVisibility(emailAddressField.findElement(By.xpath("following-sibling::div[1]")))).equals(message);
            case "First Name":
                return getTextFromElement(waitForVisibility(firstNameField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "Last Name":
                return getTextFromElement(waitForVisibility(lastNameField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "Company":
                return getTextFromElement(waitForVisibility(companyField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "First Address":
                return getTextFromElement(waitForVisibility(firstStreetAddressField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "Second Address":
                return getTextFromElement(waitForVisibility(secondStreetAddressField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "Third Address":
                return getTextFromElement(waitForVisibility(thirdStreetAddressField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "City":
                return getTextFromElement(waitForVisibility(cityField.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "State/Province":
                return getTextFromElement(waitForVisibility(stateProvinceDropdown.findElement(By.xpath("following-sibling::div")))).equals(message);
            case "Zip/Postal Code":
                return getTextFromElement(waitForVisibility(zipPostalCodeField.findElement(By.xpath("following-sibling::div")))).equals(message);
            default:
                return getTextFromElement(waitForVisibility(phoneNumberField.findElement(By.xpath("following-sibling::div[2]")))).equals(message);
        }
    }

    public void clickOnButton(String button) {
        switch (button) {
            case "Next":
                clickOn(nextButton);
                waitForLoadingToFinish();
                break;
            default:
                clickOn(itemInCartButton);
        }
    }

    public ShippingDetails selectShippingMethod(int shippingMethodIndex) {
        clickOn(waitForVisibilityOfAllElements(shippingMethodsList).get(shippingMethodIndex));
        waitForLoadingToFinish();

        ShippingDetails shipping = new ShippingDetails();
        shipping.setPrice(getTextFromElement(shippingMethodsList.get(shippingMethodIndex).findElement(By.xpath("td[2]/span/span"))));
        shipping.setMethod(getTextFromElement(shippingMethodsList.get(shippingMethodIndex).findElement(By.xpath("td[3]"))));
        shipping.setCarrier(getTextFromElement(shippingMethodsList.get(shippingMethodIndex).findElement(By.xpath("td[4]"))));

        return shipping;
    }

    public void clearField(String field) {
        switch (field) {
            case "Email":
                clearField(emailAddressField);
                break;
            case "First Name":
                clearField(firstNameField);
                break;
            default:
                clearField(lastNameField);
        }
    }

    public void enterInputInField(String field, String input) {
        switch (field) {
            case "Email":
                enterInputInField(emailAddressField, input);
                break;
            case "First Name":
                enterInputInField(firstNameField, input);
                break;
            case "Last Name":
                enterInputInField(lastNameField, input);
                break;
            case "Company":
                enterInputInField(companyField, input);
                break;
            case "First Address":
                enterInputInField(firstStreetAddressField, input);
                break;
            case "Second Address":
                enterInputInField(secondStreetAddressField, input);
                break;
            case "Third Address":
                enterInputInField(thirdStreetAddressField, input);
                break;
            case "Zip/Postal Code":
                enterInputInField(zipPostalCodeField, input);
                break;
            case "City":
                enterInputInField(cityField, input);
                break;
            default:
                enterInputInField(phoneNumberField, input);
        }
    }

    public void clickOnTooltip(int tooltipIndex) {
        clickOn(tooltipsList.get(tooltipIndex));
    }

    public boolean checkIfTooltipIsDisplayed(int tooltipIndex, String tooltipContent) {
        return getTextFromElement(tooltipsContentList.get(tooltipIndex)).equals(tooltipContent);
    }

    public void clickOnViewDetailsButton(int itemIndex) {
        waitForVisibilityOfAllElements(viewDetailsButtonsList);
        clickOn(viewDetailsButtonsList.get(itemIndex));
    }

    public String getNumberOfItems() {
        return getTextFromElement(itemCounter);
    }

    public WebElement getItemInformation(int itemIndex) {
        waitForLoadingToFinish();
        return waitForVisibilityOfAllElements(itemInformationList).get(itemIndex);
    }

    public void selectStateProvince(String value) {
        new Select(waitForVisibility(stateProvinceDropdown)).selectByVisibleText(value);
    }
}
